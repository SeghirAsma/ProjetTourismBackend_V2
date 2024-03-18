package com.TourismApp.TourismApplication.Repositories;

import com.TourismApp.TourismApplication.DTO.SignUp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserAuthenRepository extends MongoRepository<SignUp, String> {
}
