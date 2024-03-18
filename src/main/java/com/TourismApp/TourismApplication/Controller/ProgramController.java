package com.TourismApp.TourismApplication.Controller;

import com.TourismApp.TourismApplication.Models.Item;
import com.TourismApp.TourismApplication.Models.Program;
import com.TourismApp.TourismApplication.Services.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/programs")
public class ProgramController {
    @Autowired
    private ProgramService programService;
    @Autowired
    public ProgramController(ProgramService programService){
        this.programService=programService;
    }

    @GetMapping("/gelAllPrograms")
    public ResponseEntity<List<Program>> getAllPrograms() {
        List<Program> programs = programService.getAllPrograms();
        return new ResponseEntity<>(programs, HttpStatus.OK);
    }

    @GetMapping("/program/{idProgram}")
    public ResponseEntity<Program> getProgramById(@PathVariable String idProgram) {
        Optional<Program> program = programService.getProgramById(idProgram);
        return program.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /*@PostMapping("/createProgram")
    public ResponseEntity<Program> createPrograms(@RequestBody Program program) {
        try {
            Program createdProgram = programService.createProgram(program);
            return new ResponseEntity<>(createdProgram, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }*/

    @PostMapping("/createProgram")
    public ResponseEntity<Program> createPrograms(@RequestBody Program program,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails.getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().equals("admin") || role.getAuthority().equals("agence"))
            ) {
                program.setApproved(true);
            } else if (userDetails.getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().equals("createur"))
            ) {
                program.setApproved(false);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            Program createdProgram = programService.createProgram(program);

            return new ResponseEntity<>(createdProgram, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @PutMapping("/updateProgram/{idProgram}")
    public ResponseEntity<Program> updateProgramById(@PathVariable String idProgram, @RequestBody Program updatedProgram) {
        return programService.updateProgramById(idProgram, updatedProgram);
    }

    @PutMapping("archiveProgram/{idProgram}")
    public ResponseEntity<Program> archiveitem(@PathVariable String idProgram){
        boolean success = programService.archiveProgram(idProgram);
        if(success){
            return  ResponseEntity.ok().build();
        }
        else {
            return  ResponseEntity.notFound().build();
        }
    }

    @GetMapping("isarchiveProgram")
    public ResponseEntity<List<Program>> findArchivePrograms(){
        List<Program> archivedPrograms = programService.findArchivedProgram();
        return ResponseEntity.ok(archivedPrograms);
    }

    @DeleteMapping("/deleteProgram/{idProgram}")
    public ResponseEntity<Void> deleteItem(@PathVariable String idProgram) {
        programService.deleteProgram(idProgram);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

   /* @PutMapping("/approveProgram/{idProgram}")
    public ResponseEntity<Void> approveUser(@PathVariable String idProgram) {
        try {
            programService.approveProgram(idProgram);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } */
   @PutMapping("/approveProgram/{idProgram}")
   public ResponseEntity<Void> approveUser(@PathVariable String idProgram, @AuthenticationPrincipal UserDetails userDetails) {
       try {
           if (userDetails.getAuthorities().stream()
                   .anyMatch(role -> role.getAuthority().equals("admin") || role.getAuthority().equals("agence"))
           ) {
               programService.approveProgram(idProgram);
               return new ResponseEntity<>(HttpStatus.OK);
           } else {
               return new ResponseEntity<>(HttpStatus.FORBIDDEN);
           }
       } catch (Exception e) {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
   }



}
