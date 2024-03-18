package com.TourismApp.TourismApplication.Repositories;

import com.TourismApp.TourismApplication.Models.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository  extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByFirstName(String firstName);//

    List<UserEntity> findByApprovedFalse();
    List<UserEntity> findByApprovedTrue(); //
    List<UserEntity> findByIsDeletedTrue();  //

}
