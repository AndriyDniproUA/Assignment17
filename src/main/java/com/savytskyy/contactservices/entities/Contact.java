package com.savytskyy.contactservices.entities;

import lombok.Data;

@Data
public class Contact {
    private Integer id;
    private String name;
    private ContactType type;
    private String value;

    public enum ContactType {PHONE,EMAIL};

}
