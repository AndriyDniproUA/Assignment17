package com.savytskyy.contactservices.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.services.usersservice.ApiUsersService;
import com.savytskyy.contactservices.services.usersservice.DummyUsersService;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpClient;

@RequiredArgsConstructor
public class UserServiceFactory implements UserServiceCreator{
    private final String baseURI;
    private final ObjectMapper mapper;
    private final HttpClient client;

    @Override
    public UsersService createApiUserService() {
        return new ApiUsersService(baseURI, mapper, client);
    }

    @Override
    public UsersService createDummyUsersService() {
        return new DummyUsersService();
    }
}
