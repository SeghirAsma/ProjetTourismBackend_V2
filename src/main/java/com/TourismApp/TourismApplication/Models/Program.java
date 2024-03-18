package com.TourismApp.TourismApplication.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
@Builder
@Document(collection="programme")
public class Program implements Serializable {
    @Id
    private String idProgram;
   /* @Field("reference_Program")*/
    private String referenceProgram;
  /*  @Field("name_Program") */
    private String nameProgram;
    @DBRef
    private List<Item> items;
    private boolean isDeleted = false;
    private boolean approved; //
    @DBRef
    private List<Contenu> contents;
    @Field("creation_Date")
    @CreatedDate
    private Date creationDate=new Date();

}
