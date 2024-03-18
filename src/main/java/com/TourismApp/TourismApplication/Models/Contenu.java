package com.TourismApp.TourismApplication.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection="contenus")
public class Contenu implements Serializable {
    @Id
    private String idContenu;

  /*  @Field("title_Contenu") */
    private String titleContenu;
   /* @Field("description_Contenu") */
    private String descriptionContenu;
    private String statusContenu;
    private String videoContenu;
    private String videoContenuUrl;  //
    private boolean approved ;//
    @DBRef  //
    private UserEntity userEntity;  //
    private boolean isDeleted = false; //
    @Field("creation_Date")
    @CreatedDate
    private Date creationDate=new Date();
    public Contenu(String idContenu ){
        this.idContenu=idContenu;
    }
}
