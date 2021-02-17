package com.savytskyy.contactservices.factories;

import com.savytskyy.contactservices.services.contactsservice.ContactsService;
import com.savytskyy.contactservices.services.usersservice.UsersService;

public interface ServiceFactory {
    UsersService createUserService();
    ContactsService createContactService();
}
