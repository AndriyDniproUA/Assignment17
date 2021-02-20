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

    private UsersService usersService=null;
    private ContactsService contactsService=null;
    private HttpRequestCreator httpRequestFactory;

    //private final HttpRequestCreator httpRequestCreator;
    //private final static UsersService usersService=new ApiUsersService(baseURI, mapper,client);

    @Override
    public UsersService createUserService() {
        if (usersService==null) {
            usersService = new ApiUsersService(baseURI, mapper, client,new JsonHttpRequestFactory(null,mapper,baseURI));
        }
        return usersService;
    }

    @Override
    public ContactsService createContactService() {
        if (contactsService==null) {
            contactsService = new ApiContactsService(usersService,mapper,client,
                    new JsonHttpRequestFactory(usersService,mapper,baseURI));
        }
        return contactsService;
    }
}








