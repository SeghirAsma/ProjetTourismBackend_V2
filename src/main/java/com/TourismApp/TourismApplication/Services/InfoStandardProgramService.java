package com.TourismApp.TourismApplication.Services;

/*import com.TourismApp.TourismApplication.Models.InfoStandardProgram;
import com.TourismApp.TourismApplication.Repositories.InfoStandardProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InfoStandardProgramService {
    @Autowired
    private InfoStandardProgramRepository infoStandardProgramRepository;

    public List<InfoStandardProgram> getAllInfoStandardPrograms() {
        return infoStandardProgramRepository.findAll();
    }

    public Optional<InfoStandardProgram> getInfoStandardProgramById(String id) {
        return infoStandardProgramRepository.findById(id);
    }

    public InfoStandardProgram saveInfoStandardProgram(InfoStandardProgram infoStandardProgram) {
        return infoStandardProgramRepository.save(infoStandardProgram);
    }

    public InfoStandardProgram updateInfoStandardProgram(String id, String referenceProgram, String titleProgram) {
        InfoStandardProgram existingInfoStandardProgram = infoStandardProgramRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InfoStandardProgram not found"));

        existingInfoStandardProgram.setReferenceProgram(referenceProgram);
        existingInfoStandardProgram.setTitleProgram(titleProgram);

        return infoStandardProgramRepository.save(existingInfoStandardProgram);
    }

    public void deleteInfoStandardProgramById(String id) {
        infoStandardProgramRepository.deleteById(id);
    }
}
*/