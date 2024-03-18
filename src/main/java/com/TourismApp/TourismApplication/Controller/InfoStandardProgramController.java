package com.TourismApp.TourismApplication.Controller;
/*
import com.TourismApp.TourismApplication.Models.InfoStandardProgram;
import com.TourismApp.TourismApplication.Services.InfoStandardProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/infoStandardProgram")
public class InfoStandardProgramController {
    @Autowired
    private InfoStandardProgramService infoStandardProgramService;

    @GetMapping("/getallinfoprogram")
    public List<InfoStandardProgram> getAllInfoStandardPrograms() {
        return infoStandardProgramService.getAllInfoStandardPrograms();
    }

    @GetMapping("/getinfoprogrambyid/{id}")
    public Optional<InfoStandardProgram> getInfoStandardProgramById(@PathVariable String id) {
        return infoStandardProgramService.getInfoStandardProgramById(id);
    }

    @PostMapping("/createinfoprogram")
    public InfoStandardProgram saveInfoStandardProgram(@RequestBody InfoStandardProgram infoStandardProgram) {
        return infoStandardProgramService.saveInfoStandardProgram(infoStandardProgram);
    }

    @PutMapping("/updateinfoprogram/{id}")
    public ResponseEntity<InfoStandardProgram> updateInfoStandardProgram(@PathVariable String id, @RequestBody InfoStandardProgram infoStandardProgram) {
        InfoStandardProgram updatedInfoStandardProgram = infoStandardProgramService.updateInfoStandardProgram(
                id,
                infoStandardProgram.getReferenceProgram(),
                infoStandardProgram.getTitleProgram()
        );
        return new ResponseEntity<>(updatedInfoStandardProgram, HttpStatus.OK);
    }

    @DeleteMapping("/deleteinfoprogram/{id}")
    public ResponseEntity<String> deleteInfoStandardProgram(@PathVariable String id) {
        infoStandardProgramService.deleteInfoStandardProgramById(id);
        return new ResponseEntity<>("InfoStandardProgram deleted successfully", HttpStatus.OK);
    }

}
*/