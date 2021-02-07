package com.savytskyy.contactservices.services.contactsservice;

import com.savytskyy.contactservices.entities.Contact;

import java.util.List;

public interface ContactsService {
    void add (Contact contact);
    void remove (Integer id);
    List<Contact> findByName (String name);
    List<Contact> findByValue (String value);
    List<Contact> getAll ();
}
