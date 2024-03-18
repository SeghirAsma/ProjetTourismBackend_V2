package com.TourismApp.TourismApplication.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="users")
public class UserEntity implements Serializable {
   @Id
   private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    @DBRef
    private List<Contenu> contenus;

    private boolean approved ; //
    private boolean isDeleted = false; //

 public String profileImageUrl; //
 @Field("creation_Date")
 @CreatedDate
 private Date creationDate=new Date();



}
