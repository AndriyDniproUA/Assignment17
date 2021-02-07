package com.savytskyy.contactservices.menu.contactsmenu;

import com.savytskyy.contactservices.menu.MenuItem;
import com.savytskyy.contactservices.services.contactsservice.ContactsService;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class FindContactByValueMenuItem implements MenuItem {

    private final UsersService usersService;
    private final ContactsService contactsService;

    Scanner sc = new Scanner(System.in);

    @Override
    public void doAction() {
        System.out.print("Please enter the part of the contact value to find: ");
        String value = sc.nextLine();

        System.out.println("---------------------------------");
            contactsService.findByValue(value).stream()
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
        return "Find contact by the part of its value";
    }

    @Override
    public boolean closeAfter() {
        return false;
    }

    @Override
    public boolean isVisible() {return usersService.isAuth();}


}
