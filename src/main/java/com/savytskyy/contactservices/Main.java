package com.savytskyy.contactservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.config.AppConfiguration;
import com.savytskyy.contactservices.config.AppProperties;
import com.savytskyy.contactservices.config.ConfigLoader;
import com.savytskyy.contactservices.factories.*;
import com.savytskyy.contactservices.menu.contactsmenu.*;
import com.savytskyy.contactservices.menu.usersmenu.LoginUserMenuItem;
import com.savytskyy.contactservices.menu.usersmenu.ReadAllUsersMenuItem;
import com.savytskyy.contactservices.menu.usersmenu.RegisterUserMenuItem;
import com.savytskyy.contactservices.services.contactsservice.*;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import com.savytskyy.contactservices.utils.*;

import java.net.http.HttpClient;

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

        ObjectMapper mapper = new ObjectMapper();
        HttpClient client = HttpClient.newBuilder().build();

        UserServiceCreator userServiceFactory = new UserServiceFactory(baseURI,mapper,client);
        ContactServiceCreator contactServiceFactory = new ContactServiceFactory();


        UsersService usersService = null;
        ContactsService contactsService = null;


        if (mode.equals("api")) {
            System.out.println("Starting online (API) contact manager...");
            usersService = userServiceFactory.createApiUserService();
            HttpRequestCreator httpRequestCreator = new JsonHttpRequestFactory(usersService,mapper,baseURI);
            contactsService = contactServiceFactory.createApiContactsService(mapper, client, httpRequestCreator);

            //usersService = new ApiUsersService(baseURI, mapper, client);
            //contactsService = new ApiContactsService(mapper, client, httpRequestFactory);

        } else if (mode.equals("file")) {
            System.out.println("Starting NIO file contact manager...");
            ContactSerializer contactSerializer = new DefaultContactSerializer();
            usersService = userServiceFactory.createDummyUsersService();
            contactsService = contactServiceFactory.createNioFileContactsService(contactSerializer,filePath);

            //usersService = new DummyUsersService();
            //contactsService = new NioFileContactsService(contactSerializer, filePath);

        } else if (mode.equals("memory")) {
            System.out.println("Starting in memory (RAM) contact manager...");
            usersService = userServiceFactory.createDummyUsersService();
            contactsService = contactServiceFactory.createInMemoryContactsService();
            //usersService = new DummyUsersService();
            //contactsService = new InMemoryContactsService();
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

















