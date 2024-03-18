package com.TourismApp.TourismApplication.Controller;
import com.TourismApp.TourismApplication.Configuration.UserInfoUserDetails;
import com.TourismApp.TourismApplication.Models.Item;
import com.TourismApp.TourismApplication.Models.UserEntity;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;

import com.TourismApp.TourismApplication.Models.Contenu;
import com.TourismApp.TourismApplication.Services.ContenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contenus")
@CrossOrigin(origins = "http://localhost:3000")
public class ContenuController {
    private final ContenuService contenuService;

    @Autowired
    public ContenuController(ContenuService contenuService) {
        this.contenuService = contenuService;

    }



    @GetMapping("/all")
    public ResponseEntity<List<Contenu>> getAllContenus() {
        List<Contenu> contenus = contenuService.getAllContenus();
        return new ResponseEntity<>(contenus, HttpStatus.OK);
    }

    @GetMapping("/contenu/{id}")
    public ResponseEntity<Contenu> getContenuById(@PathVariable String id) {
        Optional<Contenu> contenu = contenuService.getContenuById(id);
        return contenu.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/videos/{filename}")
    public ResponseEntity<Resource> serveVideo(@PathVariable String filename) throws MalformedURLException {
        Resource videoFile = contenuService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .body(videoFile);
    }

    @PostMapping("/add")
   public ResponseEntity<Contenu> createContenu(
            @RequestPart("titleContenu") String titleContenu,
           @RequestPart("descriptionContenu") String descriptionContenu,
           @RequestPart("videoContenuUrl") MultipartFile videoFile,
            @AuthenticationPrincipal UserDetails userDetails) {
        // Check if the authenticated user has the required role
        if (userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("admin") || role.getAuthority().equals("createur"))
        ) {

            // Extract user information from UserDetails
            UserEntity authenticatedUser = ((UserInfoUserDetails) userDetails).getUserEntity();

            // Call the service method to create the video
            Contenu createdContenu = contenuService.createContenu(
                    titleContenu, descriptionContenu, videoFile, authenticatedUser);

            return new ResponseEntity<>(createdContenu, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
   }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContenu(@PathVariable String id) {
        contenuService.deleteContenu(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Contenu> updateContenuById(@PathVariable String id, @RequestBody Contenu updatedContenu) {
        return contenuService.updateContenuById(id, updatedContenu);
    }

    @PutMapping("/approve/{idContenu}")
    public ResponseEntity<Void> approveUser(@PathVariable String idContenu) {
        try {
            contenuService.approveContenu(idContenu);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/unapproved")
    public ResponseEntity<List<Contenu>> getAllUnapprovedContenus() {
        List<Contenu> unapprovedContenus = contenuService.getAllUnapprovedContenus();
        return ResponseEntity.ok(unapprovedContenus);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<Contenu>> getAllApprovedContenus() {
        List<Contenu> approvedContenus = contenuService.getAllApprovedContenus();
        return ResponseEntity.ok(approvedContenus);
    }

    @PutMapping("archiveContenu/{id}")
    public ResponseEntity<Contenu> archiveContenu(@PathVariable String id){
        boolean success = contenuService.archiveContenu(id);
        if(success){
            return  ResponseEntity.ok().build();
        }
        else {
            return  ResponseEntity.notFound().build();
        }
    }

    @GetMapping("isarchiveContenu")
    public ResponseEntity<List<Contenu>> findArchiveContenu(){
        List<Contenu> archiveedContenus = contenuService.findArchivedContenu();
        return ResponseEntity.ok(archiveedContenus);
    }

    @GetMapping("isunarchiveContenu")
    public ResponseEntity<List<Contenu>> findUnArchiveContenu(){
        List<Contenu> unarchiveedContenus = contenuService.findUnArchivedContenu();
        return ResponseEntity.ok(unarchiveedContenus);
    }

    @GetMapping("/getContenusByIds")
    public ResponseEntity<List<Contenu>> getContenusByIds(@RequestParam List<String> contenuIds) {
        List<Contenu> contenus = contenuService.getContentsByIds(contenuIds);

        if (!contenus.isEmpty()) {
            return new ResponseEntity<>(contenus, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}


 /* @GetMapping(value = "/videos/all", produces = "multipart/mixed")
   public ResponseEntity<List<Resource>> serveAllVideos() {
       List<Resource> videoFiles = contenuService.loadAllVideos();

      HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.MULTIPART_MIXED);

       return new ResponseEntity<>(videoFiles, headers, HttpStatus.OK);

   } */