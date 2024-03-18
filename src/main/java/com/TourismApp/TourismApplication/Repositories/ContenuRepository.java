package com.TourismApp.TourismApplication.Repositories;

import com.TourismApp.TourismApplication.Models.Contenu;
import com.TourismApp.TourismApplication.Models.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenuRepository extends MongoRepository<Contenu,String> {
    List<Contenu> findByApprovedFalse();
    List<Contenu> findByApprovedTrue();
    List<Contenu> findByIsDeletedTrue();  //
    List<Contenu> findByIsDeletedFalse(); //
}
