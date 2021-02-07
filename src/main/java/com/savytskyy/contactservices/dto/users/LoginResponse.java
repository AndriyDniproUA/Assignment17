package com.savytskyy.contactservices.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    private String token;
    private String status;
    private String error;
}
