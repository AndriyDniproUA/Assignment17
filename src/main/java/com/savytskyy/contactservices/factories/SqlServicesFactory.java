package com.savytskyy.contactservices.factories;


import com.savytskyy.contactservices.services.contactsservice.ContactsService;
import com.savytskyy.contactservices.services.contactsservice.SqlContactsService;
import com.savytskyy.contactservices.services.usersservice.SqlUsersService;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;


@RequiredArgsConstructor
public class SqlServicesFactory implements ServiceFactory {
    private final DataSource dataSource;

    private UsersService usersService = null;
    private ContactsService contactsService = null;

    @Override
    public UsersService createUserService() {
        if (usersService == null) {
            usersService = new SqlUsersService(dataSource);
        }
        return usersService;
    }

    @Override
    public ContactsService createContactService() {
        if (contactsService == null) {
            usersService = createUserService();
            contactsService = new SqlContactsService(usersService, dataSource);
        }
        return contactsService;
    }
}








