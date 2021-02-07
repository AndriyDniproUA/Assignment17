package com.savytskyy.contactservices.dto.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class AddContactRequest {
    private String name;
    private String type;
    private String value;

}
