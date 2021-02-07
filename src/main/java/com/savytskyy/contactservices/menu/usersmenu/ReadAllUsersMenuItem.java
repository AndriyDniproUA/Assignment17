package com.savytskyy.contactservices.menu.usersmenu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.menu.MenuItem;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpClient;
import java.util.List;

@RequiredArgsConstructor
public class ReadAllUsersMenuItem implements MenuItem {
    private final UsersService usersService;


    @Override
    public void doAction() {

        System.out.println("---------------------------------");
        usersService.getAll().stream()
                .forEach(u -> System.out.printf(
                        "Login: %s, Date of birth: %s\n",
                        u.getLogin(),
                        u.getDateOfBirth()));
        System.out.println("---------------------------------");

    }

    @Override
    public String getName() {
        return "Read all users";
    }

    @Override
    public boolean closeAfter() {
        return false;
    }


    @Override
    public boolean isVisible() {
        return !usersService.isAuth();
    }


}
