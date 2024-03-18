package com.TourismApp.TourismApplication.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@Builder
public class Utilisateur extends UserEntity implements Serializable {
   /* @Id
    private Long idCreator; */
}
