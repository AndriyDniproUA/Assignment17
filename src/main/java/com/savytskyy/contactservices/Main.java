package com.savytskyy.contactservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.annotations.SystemProp;
import com.savytskyy.contactservices.config.AppConfiguration;
import com.savytskyy.contactservices.config.AppProperties;
import com.savytskyy.contactservices.config.ConfigLoader;
import com.savytskyy.contactservices.dto.contacts.AddContactRequest;
import com.savytskyy.contactservices.entities.Contact;
import com.savytskyy.contactservices.entities.User;
import com.savytskyy.contactservices.menu.contactsmenu.*;
import com.savytskyy.contactservices.menu.usersmenu.LoginUserMenuItem;
import com.savytskyy.contactservices.menu.usersmenu.ReadAllUsersMenuItem;
import com.savytskyy.contactservices.menu.usersmenu.RegisterUserMenuItem;
import com.savytskyy.contactservices.services.contactsservice.*;
import com.savytskyy.contactservices.services.usersservice.ApiUsersService;
import com.savytskyy.contactservices.services.usersservice.DummyUsersService;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import com.savytskyy.contactservices.utils.ContactSerializer;
import com.savytskyy.contactservices.utils.DefaultContactSerializer;
import com.savytskyy.contactservices.utils.NioFileUtil;

import java.io.*;
import java.lang.reflect.*;
import java.net.http.HttpClient;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        ConfigLoader configLoader = new ConfigLoader();
        AppProperties p = configLoader.getSystemProps(AppProperties.class);
        String configFileName = "app-" + p.getProfile() + ".properties";
        AppConfiguration config = configLoader
                .getFileProps(AppConfiguration.class, configFileName);

        String mode = config.getWorkMode();
        String baseURI = config.getBaseURI();
        String filePath = config.getFilePath();

        UsersService usersService = null;
        ContactsService contactsService = null;


        if (mode.equals("api")) {
            System.out.println("Starting online (API) contact manager...");
            ObjectMapper mapper = new ObjectMapper();
            HttpClient client = HttpClient.newBuilder().build();
            usersService = new ApiUsersService(baseURI, mapper, client);
            contactsService = new ApiContactsService(usersService, mapper, client, baseURI);
        } else if (mode.equals("file")) {
            System.out.println("Starting NIO file contact manager...");
            ContactSerializer contactSerializer = new DefaultContactSerializer();
            usersService = new DummyUsersService();
            contactsService = new NioFileContactsService(contactSerializer, filePath);
        } else if (mode.equals("memory")) {
            System.out.println("Starting in memory (RAM) contact manager...");
            usersService = new DummyUsersService();
            contactsService = new InMemoryContactsService();
        } else {
            throw new RuntimeException(mode + " is not supported work mode!");
        }

//            //FOR File service
//            ContactSerializer contactSerializer = new DefaultContactSerializer();
//            String filePath = "contacts.txt";
//            UsersService usersService = new DummyUsersService();
//            ContactsService contactsService = new FileContactsService(contactSerializer,filePath);

        Menu menu = new Menu();
        menu.addMenuItem(new ReadAllContactsMenuItem(usersService, contactsService));
        menu.addMenuItem(new FindContactByNameMenuItem(usersService, contactsService));
        menu.addMenuItem(new FindContactByValueMenuItem(usersService, contactsService));
        menu.addMenuItem(new AddContactMenuItem(usersService, contactsService));
        menu.addMenuItem(new RemoveContactMenuItem(usersService, contactsService));

        menu.addMenuItem(new LoginUserMenuItem(usersService));
        menu.addMenuItem(new RegisterUserMenuItem(usersService));
        menu.addMenuItem(new ReadAllUsersMenuItem(usersService));

        menu.addMenuItem(new QuitMenuItem());
        menu.run();
    }
}

















