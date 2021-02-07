package com.savytskyy.contactservices.dto.users;

import lombok.Data;

@Data
public class AddUserRequest {
    private String login;
    private String password;
    private String dateOfBirth;

}
