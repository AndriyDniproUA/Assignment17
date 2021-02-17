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

        //UserServiceCreator userServiceFactory = new UserServiceFactory(baseURI,mapper,client);
        //ContactServiceCreator contactServiceFactory = new ContactServiceFactory();
        //UsersService usersService = null;
        //ContactsService contactsService = null;
        ServiceFactory serviceFactory;
        UsersService usersService;
        ContactsService contactsService;


        if (mode.equals("api")) {
            System.out.println("Starting online (API) contact manager...");
            //HttpRequestCreator httpRequestCreator = new JsonHttpRequestFactory(mapper,baseURI);

            serviceFactory = new ApiServicesFactory(baseURI,mapper,client);
//            usersService = serviceFactory.createUserService();
//            contactsService = serviceFactory.createContactService();

        } else if (mode.equals("file")) {
            System.out.println("Starting NIO file contact manager...");
            ContactSerializer contactSerializer = new DefaultContactSerializer();

            serviceFactory = new NioFileServicesFactory(contactSerializer,filePath);
//            usersService = serviceFactory.createUserService();
//            contactsService = serviceFactory.createContactService();

        } else if (mode.equals("memory")) {
            System.out.println("Starting in memory (RAM) contact manager...");

            serviceFactory = new InMemoryServicesFactory();
//            usersService = serviceFactory.createUserService();
//            contactsService = serviceFactory.createContactService();

        } else {
            throw new RuntimeException(mode + " is not supported work mode!");
        }

//            //FOR File service
//            ContactSerializer contactSerializer = new DefaultContactSerializer();
//            String filePath = "contacts.txt";
//            UsersService usersService = new DummyUsersService();
//            ContactsService contactsService = new FileContactsService(contactSerializer,filePath);

        usersService = serviceFactory.createUserService();
        contactsService = serviceFactory.createContactService();

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

















