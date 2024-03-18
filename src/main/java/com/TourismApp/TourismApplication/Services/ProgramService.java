package com.TourismApp.TourismApplication.Services;

import com.TourismApp.TourismApplication.Models.Contenu;
import com.TourismApp.TourismApplication.Models.Item;
import com.TourismApp.TourismApplication.Models.Program;
import com.TourismApp.TourismApplication.Repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgramService {
    @Autowired
    private final ProgramRepository programRepository;
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final ContenuService contenuService;


    @Autowired
    public ProgramService(ProgramRepository programRepository,ItemService itemService,ContenuService contenuService) {
        this.programRepository = programRepository;
        this.itemService=itemService;
        this.contenuService=contenuService;
    }

    public List<Program> getAllPrograms(){
        return programRepository.findAll();
    }
    public Optional<Program> getProgramById(String idProgram){

        return programRepository.findById(idProgram);
    }


   public Program createProgram(Program program){
        try {
            List<String> itemIds = program.getItems().stream()
                    .map(Item::getIdItem)
                    .collect(Collectors.toList());
            List<Item> items = itemService.getItemsByIds(itemIds);
            program.setItems(items);

            List<String> contenuIds = program.getContents().stream() // Add this block
                    .map(Contenu::getIdContenu)
                    .collect(Collectors.toList());
            List<Contenu> contents = contenuService.getContentsByIds(contenuIds);
            program.setContents(contents);

            System.out.println("Received Program: " + program.toString());
            return programRepository.save(program);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création du programme : " + e.getMessage());
        }
    }


    public ResponseEntity<Program> updateProgramById(String id, Program updatedProgram) {
        Optional<Program> existingProgramOptional = programRepository.findById(id);
        if (existingProgramOptional.isPresent()) {
            Program existingProgram = existingProgramOptional.get();
            existingProgram.setReferenceProgram(updatedProgram.getReferenceProgram());
            existingProgram.setNameProgram((updatedProgram.getNameProgram()));
            existingProgram.setItems(updatedProgram.getItems());
            existingProgram.setContents(updatedProgram.getContents());
            Program savedProgram = programRepository.save(existingProgram);
            return new ResponseEntity<>(savedProgram, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteProgram(String idProgram) {
        programRepository.deleteById(idProgram);
    }

    public boolean archiveProgram(String idProgram){
        if(programRepository.findById(idProgram).isPresent()){
            Program program = programRepository.findById(idProgram).get();
            program.setDeleted(true);
            programRepository.save(program);
            return  true;
        }
        return  false;
    }

    public List<Program> findArchivedProgram(){
        return programRepository.findByIsDeletedTrue();
    }

    public void approveProgram(String idProgram) {
        Program program = programRepository.findById(idProgram)
                .orElseThrow(() -> new RuntimeException("Program not found"));
        program.setApproved(true);
        programRepository.save(program);

    }

}
