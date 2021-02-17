package com.savytskyy.contactservices.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.services.usersservice.ApiUsersService;
import com.savytskyy.contactservices.services.usersservice.UsersService;

import java.net.http.HttpClient;

public interface UserServiceCreator {
    UsersService createApiUserService();
    UsersService createDummyUsersService();
}
