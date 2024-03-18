package com.TourismApp.TourismApplication.Services;

import com.TourismApp.TourismApplication.DTO.SignUp;
import com.TourismApp.TourismApplication.EmailSending.UserApprovedEvent;
import com.TourismApp.TourismApplication.Models.UserEntity;
import com.TourismApp.TourismApplication.Repositories.UserAuthenRepository;
import com.TourismApp.TourismApplication.Repositories.UserEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private EmailService emailService;
    ///
    @Value("${uploadImage.dir}")
    private String uploadDirImage;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    public UserService(UserEntityRepository userEntityRepository,
                       EmailService emailService ){
        this.userEntityRepository=userEntityRepository;
        this.emailService=emailService;
    }

    public UserEntity saveUser(UserEntity user){
        return  userEntityRepository.save(user);
    }

    public List<UserEntity> getAllUsers(){
        return userEntityRepository.findAll();
    }

    public Optional<UserEntity> getUserById(String userId) {
        return userEntityRepository.findById(userId);
    }

    public void deleteUserById(String userId) {
        userEntityRepository.deleteById(userId);
    }

    public ResponseEntity<UserEntity> updateUserById(String userId, UserEntity updatedUser) {
        Optional<UserEntity> existingUserOptional = userEntityRepository.findById(userId);

        if (existingUserOptional.isPresent()) {
            UserEntity existingUser = existingUserOptional.get();
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
          /*  existingUser.setProfileImageUrl((updatedUser.getProfileImageUrl())); */
            UserEntity savedUser = userEntityRepository.save(existingUser);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    public UserEntity getUserByEmail(String email) {
        return userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'adresse e-mail: " + email));
    }

   public UserEntity signup(UserEntity user) {
       // Vérifiez si un utilisateur avec la même adresse e-mail existe déjà
       Optional<UserEntity> existingUserOptional = userEntityRepository.findByEmail(user.getEmail());
       if (existingUserOptional.isPresent()) {
           // Si l'utilisateur existe, mettez à jour les informations au lieu de créer un nouvel utilisateur
           UserEntity existingUser = existingUserOptional.get();
           existingUser.setFirstName(user.getFirstName());
           existingUser.setLastName(user.getLastName());
           existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
           if (existingUser.getRole().equalsIgnoreCase("admin")) {
               existingUser.setApproved(true);
           } else {
               existingUser.setApproved(true);
           }
           return userEntityRepository.save(existingUser);
       } else {
           // Sinon, créez un nouvel utilisateur
           user.setPassword(passwordEncoder.encode(user.getPassword()));
           if (user.getRole().equalsIgnoreCase("admin")) {
               user.setApproved(true);
           } else {
               user.setApproved(false);
           }
           return userEntityRepository.save(user);
       }
   }

   public ResponseEntity<String> approveUser(String userId) {
       try {
           UserEntity user = userEntityRepository.findById(userId)
                   .orElseThrow(() -> new RuntimeException("User not found"));
           user.setApproved(true);
           userEntityRepository.save(user);
           // Envoyer un e-mail d'approbation
           String userEmail = user.getEmail();
           String subject = "Account Approved";
           String body = "Dear user, your account has been approved. You can now log in.";
           emailService.sendEmail(userEmail, subject, body);
            // Déclencher l'événement d'approbation
           eventPublisher.publishEvent(new UserApprovedEvent(this, userEmail));
           return new ResponseEntity<>("User approved successfully", HttpStatus.OK);
       } catch (Exception e) {
           return new ResponseEntity<>("Error approving user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }

    public Optional<UserEntity> getUserByFirstName(String firstName) {
        return userEntityRepository.findByFirstName(firstName);
    }

    public List<UserEntity> getAllUnapprovedUsers() {
        return userEntityRepository.findByApprovedFalse();
    }
    //
    public boolean archiveUser(String id){
        if(userEntityRepository.findById(id).isPresent()){
            UserEntity user= userEntityRepository.findById(id).get();
            user.setDeleted(true);
            userEntityRepository.save(user);
            return  true;
        }
        return  false;
    }

    //
    public List<UserEntity> findArchivedUser(){
        return userEntityRepository.findByIsDeletedTrue();
    }

    private String saveProfileImage(MultipartFile profileImage) throws IOException {
        File directory = new File(uploadDirImage);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String fileName = StringUtils.cleanPath(profileImage.getOriginalFilename());
        String uniqueFileName =   fileName;
        Path targetPath = Path.of(uploadDirImage, uniqueFileName);
        Files.copy(profileImage.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return uploadDirImage + uniqueFileName;

    }

    public UserEntity updateUserProfile(String userId, MultipartFile profileImage) {
        try {
            Optional<UserEntity> existingUserOptional = userEntityRepository.findById(userId);
            if (existingUserOptional.isPresent()) {
                UserEntity existingUser = existingUserOptional.get();
                String profileImageUrl = saveProfileImage(profileImage);
                existingUser.setProfileImageUrl(profileImageUrl);
                UserEntity savedUser = userEntityRepository.save(existingUser);
                return savedUser;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId);
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving profile image", e);
        }
    }

    public List<UserEntity> getAllApprovedUsers() {
        return userEntityRepository.findByApprovedTrue();
    }

}

