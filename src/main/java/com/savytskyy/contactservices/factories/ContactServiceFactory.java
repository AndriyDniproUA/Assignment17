package com.savytskyy.contactservices.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.services.contactsservice.*;
import com.savytskyy.contactservices.utils.ContactSerializer;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpClient;

@RequiredArgsConstructor
public class ContactServiceFactory implements ContactServiceCreator{
    @Override
    public ContactsService createApiContactsService(ObjectMapper mapper, HttpClient client, HttpRequestCreator httpRequestCreator) {
        return new ApiContactsService(mapper, client, httpRequestCreator);
    }

    @Override
    public ContactsService createFileContactsService(ContactSerializer contactSerializer, String filePath) {
        return new FileContactsService(contactSerializer, filePath);
    }

    @Override
    public ContactsService createInMemoryContactsService() {
        return new InMemoryContactsService();
    }

    @Override
    public ContactsService createNioFileContactsService(ContactSerializer contactSerializer, String filePath) {
        return new NioFileContactsService(contactSerializer, filePath);
    }
}
