package com.savytskyy.contactservices.services.contactsservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.dto.GetStatusResponse;
import com.savytskyy.contactservices.dto.contacts.AddContactRequest;
import com.savytskyy.contactservices.dto.contacts.FindContactByNameRequest;
import com.savytskyy.contactservices.dto.contacts.FindContactByValueRequest;
import com.savytskyy.contactservices.dto.contacts.GetContactsResponse;
import com.savytskyy.contactservices.entities.Contact;
import com.savytskyy.contactservices.factories.HttpRequestCreator;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ApiContactsService implements ContactsService {
    private final UsersService usersService;
    private final ObjectMapper mapper;
    private final HttpClient client;
    //private final String baseURI;
    private final HttpRequestCreator httpRequestCreator;

    @Override
    public void add(Contact contact) {
        AddContactRequest request = new AddContactRequest();
        request.setName(contact.getName());
        request.setType(contact.getType().toString().toLowerCase());
        request.setValue(contact.getValue());

        try {
            HttpRequest httpRequest = httpRequestCreator.createAuthorizedPostRequest("/contacts/add",request);
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            GetStatusResponse response = mapper.readValue(httpResponse.body(), GetStatusResponse.class);
            if (response.getStatus().equals("error")) {
                throw new RuntimeException("contact not added: " + response.getError());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void remove(Integer id) {
        throw new UnsupportedOperationException("Api currently does not support this operation!");
    }

    @Override
    public List<Contact> findByName(String name) {
        FindContactByNameRequest request = new FindContactByNameRequest();
        request.setName(name);

        System.out.println(request);

        try {
            HttpRequest httpRequest = httpRequestCreator.createAuthorizedPostRequest("/contacts/find", request);
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            GetContactsResponse response = mapper.readValue(httpResponse.body(), GetContactsResponse.class);

            if (response.getStatus().equals("error")) {
                throw new RuntimeException("contact not found!" + response.getError());
            }
            return response.getContacts().stream().map(c -> {
                Contact contact = new Contact();
                contact.setId(c.getId());
                contact.setName(c.getName());
                contact.setType(Contact.ContactType.valueOf(c.getType().toUpperCase()));
                contact.setValue(c.getValue());
                return contact;
            }).collect(Collectors.toList());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Contact> findByValue(String value) {

        FindContactByValueRequest request = new FindContactByValueRequest();
        request.setValue(value);

        try {
            HttpRequest httpRequest = httpRequestCreator.createAuthorizedPostRequest("/contacts/find", request);

            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            GetContactsResponse response = mapper.readValue(httpResponse.body(), GetContactsResponse.class);
            if (response.getStatus().equals("error")) {
                throw new RuntimeException("contact not found!" + response.getError());
            }

            return response.getContacts().stream().map(c -> {
                Contact contact = new Contact();
                contact.setId(c.getId());
                contact.setName(c.getName());
                contact.setType(Contact.ContactType.valueOf(c.getType().toUpperCase()));
                contact.setValue(c.getValue());
                return contact;
            }).collect(Collectors.toList());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Contact> getAll() {
        HttpRequest httpRequest = httpRequestCreator.createAuthorizedGetRequest("/contacts");

        try {
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            GetContactsResponse response = mapper.readValue(httpResponse.body(), GetContactsResponse.class);
            if (response.getStatus().equals("error")) {
                throw new RuntimeException("Contacts not found on the server " + response.getError());
            }

            return response.getContacts().stream().map(c -> {
                Contact contact = new Contact();
                contact.setId(c.getId());
                contact.setName(c.getName());
                contact.setType(Contact.ContactType.valueOf(c.getType().toUpperCase()));
                contact.setValue(c.getValue());
                return contact;
            }).collect(Collectors.toList());


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

//    private HttpRequest createAuthorizedRequest(String uri, Object request) throws JsonProcessingException {
//        return HttpRequest.newBuilder()
//                .uri(URI.create(baseURI + uri))
//                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(request)))
//                .header("Authorization", "Bearer " + usersService.getToken())
//                .header("Content-Type", "application/json")
//                .build();
//    }
}
