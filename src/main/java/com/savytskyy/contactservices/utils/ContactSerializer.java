package com.savytskyy.contactservices.utils;

import com.savytskyy.contactservices.entities.Contact;

public interface ContactSerializer {
    String serialize (Contact c);
    Contact deserialize (String s);

}
