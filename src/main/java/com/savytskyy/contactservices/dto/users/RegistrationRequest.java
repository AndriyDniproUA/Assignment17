package com.savytskyy.contactservices.dto.users;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RegistrationRequest {
    private String login;
    private String password;
    private String dateBorn;
}
