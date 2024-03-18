package com.TourismApp.TourismApplication.Repositories;

import com.TourismApp.TourismApplication.Models.Item;
import com.TourismApp.TourismApplication.Models.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByIsDeletedTrue();  //
    List<Item> findByIsDeletedFalse();


}
