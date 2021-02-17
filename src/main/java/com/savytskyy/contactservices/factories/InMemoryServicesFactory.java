package com.savytskyy.contactservices.factories;

import com.savytskyy.contactservices.services.contactsservice.ContactsService;
import com.savytskyy.contactservices.services.contactsservice.InMemoryContactsService;
import com.savytskyy.contactservices.services.usersservice.DummyUsersService;
import com.savytskyy.contactservices.services.usersservice.UsersService;

public class InMemoryServicesFactory implements ServiceFactory{

    @Override
    public UsersService createUserService() {
        return new DummyUsersService();
    }

    @Override
    public ContactsService createContactService() {
        return new InMemoryContactsService();
    }
}
