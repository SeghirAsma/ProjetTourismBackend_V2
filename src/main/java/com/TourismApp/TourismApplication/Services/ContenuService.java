package com.TourismApp.TourismApplication.Services;

import com.TourismApp.TourismApplication.Models.Contenu;
import com.TourismApp.TourismApplication.Models.Item;
import com.TourismApp.TourismApplication.Models.UserEntity;
import com.TourismApp.TourismApplication.Repositories.ContenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContenuService {
    @Autowired
    private final ContenuRepository contenuRepository;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(ContenuService.class);

    @Value("${upload.dir}")
    private String uploadDir;
    @Autowired
    public ContenuService(ContenuRepository contenuRepository,UserService userService){
        this.contenuRepository=contenuRepository;
        this.userService=userService;
    }

    public List<Contenu> getAllContenus() {
        Sort sortByCreationDateDesc = Sort.by(Sort.Direction.DESC, "creationDate");
        return contenuRepository.findAll(sortByCreationDateDesc);
    }

    public Optional<Contenu> getContenuById(String id) {
        return contenuRepository.findById(id);
    }

    public Contenu saveContenu(Contenu contenu) {
        return contenuRepository.save(contenu);
    }

    public List<Contenu> getContentsByIds(List<String> contenuIds) {
        return contenuRepository.findAllById(contenuIds);
    }
    public Resource loadAsResource(String filename) {
        try {
            Path videoPath = Paths.get(uploadDir).resolve(filename);
            logger.info("Chemin du fichier vidéo : {}", videoPath);
            Resource videoFile = new UrlResource(videoPath.toUri());

            if (videoFile.exists() && videoFile.isReadable()) {
                return videoFile;
            } else {
                logger.error("Could not read the video file! Path: {}", videoPath);
                throw new RuntimeException("Could not read the video file!");
            }
        } catch (MalformedURLException e) {
            logger.error("Error: {}", e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public Contenu createContenu( String titleContenu, String descriptionContenu, MultipartFile videoFile, UserEntity authenticatedUser) {
    try {

        Contenu contenu = new Contenu();
        contenu.setTitleContenu(titleContenu);
        contenu.setDescriptionContenu(descriptionContenu);
        contenu.setUserEntity(authenticatedUser);  // Associate the video with the authenticated user


        // Enregistrez le fichier sur le serveur et obtenez l'URL du fichier
        String videoContenuUrl = saveVideoFile(videoFile);
        contenu.setVideoContenuUrl(videoContenuUrl);

        Contenu contenu1 = contenuRepository.save(contenu);
        return contenu1;
    } catch (Exception e) {
        throw new RuntimeException("error", e);
    }
}
    private String saveVideoFile(MultipartFile videoFile) throws IOException {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Générez un nom de fichier unique
        String fileName = StringUtils.cleanPath(videoFile.getOriginalFilename());
        String uniqueFileName =   fileName;

        // Chemin complet du fichier sur le serveur
        Path targetPath = Path.of(uploadDir, uniqueFileName);

        // Copiez le fichier vers le répertoire de stockage
        Files.copy(videoFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        // Retournez l'URL ou le chemin du fichier
        return uploadDir + uniqueFileName;

        // Retournez l'URL ou le chemin du fichier
      //  return uploadDir + File.separator + uniqueFileName;

    }
    public void deleteContenu(String id) {
        contenuRepository.deleteById(id);
    }

    public ResponseEntity<Contenu> updateContenuById(String id, Contenu updatedContenu) {
        Optional<Contenu> existingContenuOptional = contenuRepository.findById(id);
        if (existingContenuOptional.isPresent()) {
            Contenu existingContenu = existingContenuOptional.get();
            existingContenu.setTitleContenu(updatedContenu.getTitleContenu());
            existingContenu.setDescriptionContenu(updatedContenu.getDescriptionContenu());
            existingContenu.setStatusContenu(updatedContenu.getStatusContenu());
            existingContenu.setVideoContenu(updatedContenu.getVideoContenu());  //
            Contenu savedUser = contenuRepository.save(existingContenu);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

     public void approveContenu(String idContenu) {
         logger.info("Received request to approve Contenu with ID: {}", idContenu);
         Contenu contenu = contenuRepository.findById(idContenu)
                .orElseThrow(() -> new RuntimeException("Contenu not found"));
         contenu.setApproved(true);
         contenuRepository.save(contenu);

    }

    public List<Contenu> getAllUnapprovedContenus() {
        return contenuRepository.findByApprovedFalse();
    }

    public List<Contenu> getAllApprovedContenus() {
        return contenuRepository.findByApprovedTrue();
    }

    public boolean archiveContenu(String id){
        if(contenuRepository.findById(id).isPresent()){
            Contenu contenu = contenuRepository.findById(id).get();
            contenu.setDeleted(true);
            contenuRepository.save(contenu);
            return  true;
        }
        return  false;
    }

    public List<Contenu> findArchivedContenu(){
        return contenuRepository.findByIsDeletedTrue();
    }

    public List<Contenu> findUnArchivedContenu(){
        return contenuRepository.findByIsDeletedFalse();
    }
}
    /*  public List<Resource> loadAllVideos() {
        try {
            List<Resource> videoFiles = Files.list(Paths.get(uploadDir))
                    .filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().toLowerCase().endsWith(".mp4"))
                    .map(file -> {
                        try {
                            return new UrlResource(file.toUri());
                        } catch (MalformedURLException e) {
                            logger.error("Error creating resource: {}", e.getMessage(), e);
                            throw new RuntimeException("Error creating resource: " + e.getMessage(), e);
                        }
                    })
                    .collect(Collectors.toList());
            return videoFiles;
        } catch (IOException e) {
            logger.error("Error listing video files: {}", e.getMessage(), e);
            throw new RuntimeException("Error listing video files: " + e.getMessage(), e);
        }
    } */