package com.savytskyy.contactservices.menu.contactsmenu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.menu.MenuItem;
import com.savytskyy.contactservices.services.contactsservice.ContactsService;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class ReadAllContactsMenuItem implements MenuItem {
    private final UsersService usersService;
    private final ContactsService contactsService;

    Scanner sc = new Scanner (System.in);

    @Override
    public void doAction() {
        System.out.println("---------------------------------");
        contactsService.getAll().stream()
                .forEach(s -> System.out.printf(
                        "ID: %s, Name: %s, type: %s, value: %s \n",
                        s.getId(),
                        s.getName(),
                        s.getType(),
                        s.getValue()));
        System.out.println("---------------------------------");
    }

    @Override
    public String getName() {
        return "Read all contacts";
    }

    @Override
    public boolean closeAfter() {
        return false;
    }

    @Override
    public boolean isVisible() {return usersService.isAuth();}


}
