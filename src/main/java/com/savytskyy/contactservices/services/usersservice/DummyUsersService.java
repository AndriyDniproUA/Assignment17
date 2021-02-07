package com.savytskyy.contactservices.services.usersservice;

import com.savytskyy.contactservices.entities.User;

import java.util.Collections;
import java.util.List;

public class DummyUsersService implements UsersService{
    @Override
    public String getToken() {
        return null;
    }

    @Override
    public boolean isAuth() {
        return true;
    }

    @Override
    public void register(User user) {
        throw new UnsupportedOperationException("Registration is not supported");
    }

    @Override
    public void login(User user) {
        throw new UnsupportedOperationException("Login is not supported");
    }

    @Override
    public List<User> getAll() {
        return Collections.emptyList();
    }
}
