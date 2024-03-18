package com.TourismApp.TourismApplication.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Creator extends UserEntity  implements Serializable {
   /* @Id
    private Long idCreator; */
   @Field("creatorContenus")
    @DBRef
    private List<Contenu> contenus;
}
