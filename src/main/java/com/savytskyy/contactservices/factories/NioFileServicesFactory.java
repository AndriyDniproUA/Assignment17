package com.savytskyy.contactservices.factories;

import com.savytskyy.contactservices.services.contactsservice.ContactsService;
import com.savytskyy.contactservices.services.contactsservice.FileContactsService;
import com.savytskyy.contactservices.services.usersservice.DummyUsersService;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import com.savytskyy.contactservices.utils.ContactSerializer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NioFileServicesFactory implements ServiceFactory{
    private final ContactSerializer contactSerializer;
    private final String filePath;

    @Override
    public UsersService createUserService() {
        return new DummyUsersService();
    }

    @Override
    public ContactsService createContactService() {
        return new FileContactsService(contactSerializer, filePath);
    }
}
