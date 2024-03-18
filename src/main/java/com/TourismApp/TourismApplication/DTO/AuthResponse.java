package com.TourismApp.TourismApplication.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class AuthResponse {
    private String message;
    private String token;


    public AuthResponse(String message, String token) {
        this.message = message;
        this.token = token;

    }
}
