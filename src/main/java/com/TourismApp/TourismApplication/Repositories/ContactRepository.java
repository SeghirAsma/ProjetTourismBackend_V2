package com.TourismApp.TourismApplication.Repositories;

import com.TourismApp.TourismApplication.Models.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRepository extends MongoRepository<Contact, String> {
}
