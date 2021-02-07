package com.savytskyy.contactservices.services.contactsservice;

import com.savytskyy.contactservices.entities.Contact;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InMemoryContactsService implements ContactsService {
    List<Contact> contacts = new ArrayList<>();

    @Override
    public void add(Contact contact) {
        contact.setId(newID());
        contacts.add(contact);
    }

    @Override
    public void remove(Integer id) {
        contacts = contacts.stream()
                .filter(c->c.getId()!=id)
                .collect(Collectors.toList());

    }

    @Override
    public List<Contact> findByName(String name) {
        return contacts = contacts.stream()
                .filter(c->c.getName().startsWith(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Contact> findByValue(String value) {
        return contacts = contacts.stream()
                .filter(c->c.getValue().toLowerCase().contains(value))
                .collect(Collectors.toList());
    }

    @Override
    public List<Contact> getAll() {
        return contacts;
    }

    private int newID() {
        return contacts.stream()
                .map(c -> c.getId())
                .max(Comparator.comparingInt(a -> a))
                .map(id -> id + 1)
                .orElse(1);
    }
}
