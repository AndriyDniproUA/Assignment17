package com.savytskyy.contactservices.menu.contactsmenu;

import com.savytskyy.contactservices.entities.Contact;
import com.savytskyy.contactservices.menu.MenuItem;
import com.savytskyy.contactservices.services.contactsservice.ContactsService;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class RemoveContactMenuItem implements MenuItem {
    private final UsersService usersService;
    private final ContactsService contactsService;

    Scanner sc = new Scanner(System.in);

    @Override
    public void doAction() {

        System.out.print("Please enter the ID to remove the contact: ");
        int id = sc.nextInt();
        sc.nextLine();

        contactsService.remove(id);
    }

    @Override
    public String getName() {
        return "Remove Contact";
    }

    @Override
    public boolean closeAfter() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return usersService.isAuth();
    }

}
