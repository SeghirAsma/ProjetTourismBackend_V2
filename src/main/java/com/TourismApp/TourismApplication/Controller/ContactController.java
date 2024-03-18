package com.TourismApp.TourismApplication.Controller;

import com.TourismApp.TourismApplication.Models.Contact;
import com.TourismApp.TourismApplication.Repositories.ContactRepository;
import com.TourismApp.TourismApplication.Services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "http://localhost:3000")
public class ContactController {
    private final ContactService contactService;
    private final ContactRepository contactRepository;

    @Autowired
    public ContactController(ContactService contactService,ContactRepository contactRepository) {
        this.contactService = contactService;
        this.contactRepository=contactRepository;
    }

    @PostMapping("/contact")
    public void submitContactForm(@RequestBody Contact contact) {
        contactService.sendEmailToAgency(contact);
        contactRepository.save(contact);
    }
}
