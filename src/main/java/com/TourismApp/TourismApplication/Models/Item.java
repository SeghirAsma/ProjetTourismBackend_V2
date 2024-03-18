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
@Document(collection="items")
public class Item implements Serializable {
    @Id
    private String idItem;
    private String name;
    private String type;
    private String destination;
    private Date dateDebut;
    private Date dateFin;
    private double price;
   /* @Field("reference_Item") */
    private String referenceItem;
    private boolean isDeleted = false;
    private boolean required = true;
    @Field("creation_Date")
    @CreatedDate
    private Date creationDate=new Date();

    public Item(String idItem) {
        this.idItem = idItem;
    }

}
