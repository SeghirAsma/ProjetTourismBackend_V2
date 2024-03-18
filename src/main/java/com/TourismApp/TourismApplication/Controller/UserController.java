package com.TourismApp.TourismApplication.Controller;

import com.TourismApp.TourismApplication.Configuration.UserInfoUserDetails;
import com.TourismApp.TourismApplication.Models.UserEntity;
import com.TourismApp.TourismApplication.Services.ContenuService;
import com.TourismApp.TourismApplication.Services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final String uploadDir = "src/main/resources/imagestocke/";
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

   @PostMapping("/add")
   public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
       try {
           UserEntity createdUser = userService.signup(user);
           if (createdUser.isApproved()) {
               return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
           } else {
               return new ResponseEntity<>(createdUser, HttpStatus.ACCEPTED);
           }
       } catch (Exception e) {
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }


    @GetMapping("/all")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/by/{userId}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable String userId) {
        logger.info("Fetching user with ID: {}", userId);

        return userService.getUserById(userId)
                .map(user -> {
                    logger.info("User found: {}", user);
                    return new ResponseEntity<>(user, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    logger.info("User not found with ID: {}", userId);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @GetMapping("/byFirstName/{firstName}")
    public ResponseEntity<UserEntity> getUserByFirstName(@PathVariable String firstName) {
        logger.info("Fetching user with first name: {}", firstName);

        return userService.getUserByFirstName(firstName)
                .map(user -> {
                    logger.info("User found: {}", user);
                    return new ResponseEntity<>(user, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    logger.info("User not found with first name: {}", firstName);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }


    @PutMapping("/update/{userId}")
    public ResponseEntity<UserEntity> updateUserById(@PathVariable String userId, @RequestBody UserEntity updatedUser) {
        return userService.updateUserById(userId, updatedUser);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        if (!userService.getUserById(userId).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/approve/{userId}")
    public ResponseEntity<Void> approveUser(@PathVariable String userId) {
        try {
            userService.approveUser(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/unapproved")
    public ResponseEntity<List<UserEntity>> getAllUnapprovedUsers() {
        List<UserEntity> unapprovedUsers = userService.getAllUnapprovedUsers();
        return ResponseEntity.ok(unapprovedUsers);
    }

    //
    @PutMapping("archive/{id}")
    public ResponseEntity<UserEntity> archiveUser(@PathVariable String id){
        boolean success = userService.archiveUser(id);
        if(success){
            return  ResponseEntity.ok().build();
        }
        else {
            return  ResponseEntity.notFound().build();
        }
    }

    //
    @GetMapping("isarchive")
    public ResponseEntity<List<UserEntity>> findArchiveUser(){
        List<UserEntity> archiveedUsers = userService.findArchivedUser();
        return ResponseEntity.ok(archiveedUsers);
    }

    @GetMapping("/currentUser")
    public UserInfoUserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserInfoUserDetails) {
            return (UserInfoUserDetails) authentication.getPrincipal();
        }
        return null;
    }
    @PutMapping("update-profile/{userId}")
    public ResponseEntity<UserEntity> updateUserProfile(
            @PathVariable String userId,
            @RequestParam("profileImage") MultipartFile profileImage) {
        try {
            UserEntity updatedUser = userService.updateUserProfile(userId, profileImage);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating user profile", e);
        }
    }


    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get(uploadDir).resolve(filename);
            Resource imageFile = new UrlResource(imagePath.toUri());

            if (imageFile.exists() && imageFile.isReadable()) {
                String contentType = determineContentType(filename);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(org.springframework.http.MediaType.parseMediaType(contentType));
                return ResponseEntity.ok().headers(headers).body(imageFile);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    private String determineContentType(String filename) {
        String[] parts = filename.split("\\.");
        if (parts.length > 1) {
            String extension = parts[parts.length - 1].toLowerCase();
            switch (extension) {
                case "jpg":
                case "jpeg":
                    return "image/jpeg";
                case "png":
                    return "image/png";
                default:
                    return "image/*";
            }
        }
        return "image/*";
    }

    @GetMapping("/approved")
    public ResponseEntity<List<UserEntity>> getAllApprovedUsers() {
        List<UserEntity> approvedUsers = userService.getAllApprovedUsers();
        return ResponseEntity.ok(approvedUsers);
    }

}
