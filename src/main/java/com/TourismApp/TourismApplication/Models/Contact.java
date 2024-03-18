package com.TourismApp.TourismApplication.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection="contacts")
public class Contact implements Serializable {
    @Id
    private String idcontact;
    private String firstName;
    private String lastName;
    private String email;
    private String company;
    private String phoneNumber;
    private String message;
    @Field("creation_Date")
    @CreatedDate
    private Date creationDate=new Date();
}
