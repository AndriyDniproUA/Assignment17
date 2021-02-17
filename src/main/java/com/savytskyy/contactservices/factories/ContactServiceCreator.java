package com.savytskyy.contactservices.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.services.contactsservice.ContactsService;
import com.savytskyy.contactservices.utils.ContactSerializer;

import java.net.http.HttpClient;

public interface ContactServiceCreator {
    ContactsService createApiContactsService(ObjectMapper mapper, HttpClient client, HttpRequestCreator httpRequestCreator);
    ContactsService createFileContactsService(ContactSerializer contactSerializer, String filePath);
    ContactsService createInMemoryContactsService();
    ContactsService createNioFileContactsService(ContactSerializer contactSerializer, String filePath);
}
