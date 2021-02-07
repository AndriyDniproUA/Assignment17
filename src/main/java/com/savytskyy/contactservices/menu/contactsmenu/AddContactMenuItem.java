package com.savytskyy.contactservices.menu.contactsmenu;

import com.savytskyy.contactservices.entities.Contact;
import com.savytskyy.contactservices.menu.MenuItem;
import com.savytskyy.contactservices.services.contactsservice.ContactsService;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class AddContactMenuItem implements MenuItem {
    private final UsersService usersService;
    private final ContactsService contactsService;

    Scanner sc = new Scanner(System.in);

    @Override
    public void doAction() {

        System.out.print("Please enter the Name: ");
        String name = sc.nextLine();
        System.out.print("Please enter the type [phone/email]: ");
        String type = sc.nextLine();
        System.out.print("Please enter the value: ");
        String value = sc.nextLine();

        Contact contact = new Contact();
        contact.setType(Contact.ContactType.valueOf(type.toUpperCase()));
        contact.setName(name);
        contact.setValue(value);

        contactsService.add(contact);
    }

    @Override
    public String getName() {
        return "Add Contact";
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
