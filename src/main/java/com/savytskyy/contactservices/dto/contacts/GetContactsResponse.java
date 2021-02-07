package com.savytskyy.contactservices.dto.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetContactsResponse {
    private String status;
    private String error;
    private List<Contact> contacts;

    @Data
    public static class Contact {
        private Integer id;
        private String name;
        private String value;
        private String type;
    }

}
