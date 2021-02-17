package com.savytskyy.contactservices.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.entities.User;
import com.savytskyy.contactservices.services.contactsservice.ApiContactsService;
import com.savytskyy.contactservices.services.contactsservice.ContactsService;
import com.savytskyy.contactservices.services.usersservice.ApiUsersService;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpClient;

@RequiredArgsConstructor
public class ApiServicesFactory implements ServiceFactory{
    private final String baseURI;
    private final ObjectMapper mapper;
    private final HttpClient client;

    private UsersService usersService;
    HttpRequestCreator httpRequestFactory;

    //private final HttpRequestCreator httpRequestCreator;
    //private final static UsersService usersService=new ApiUsersService(baseURI, mapper,client);

    @Override
    public UsersService createUserService() {
        httpRequestFactory = new JsonHttpRequestFactory(usersService,mapper,baseURI);
        usersService = new ApiUsersService(baseURI, mapper, client,httpRequestFactory);
        httpRequestFactory = new JsonHttpRequestFactory(usersService,mapper,baseURI);
        return usersService;
    }

    @Override
    public ContactsService createContactService() {
        //usersService = new ApiUsersService(baseURI, mapper, client);
        return new ApiContactsService(usersService,mapper,client,httpRequestFactory);
    }
}








