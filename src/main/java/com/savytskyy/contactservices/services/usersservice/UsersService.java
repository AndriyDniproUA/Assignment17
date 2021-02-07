package com.savytskyy.contactservices.services.usersservice;

import com.savytskyy.contactservices.entities.User;

import java.util.List;

public interface UsersService {
    String getToken();
    boolean isAuth();
    void register(User user);
    void login (User user);
    List<User> getAll();
}
