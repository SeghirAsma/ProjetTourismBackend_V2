package com.TourismApp.TourismApplication.Repositories;

import com.TourismApp.TourismApplication.Models.Program;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProgramRepository extends MongoRepository<Program, String> {
    List<Program> findByIsDeletedTrue();

}
