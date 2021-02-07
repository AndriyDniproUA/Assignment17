package com.savytskyy.contactservices;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        String profile = System.getProperty("contactbook.profile");
        UsersService usersService=null;
        ContactsService contactsService=null;


        String configurationFile = null;
        if (profile.equals("dev")) configurationFile = "app-dev.properties";
        if (profile.equals("prod")) configurationFile = "app-prod.properties";

        Properties sprop = new Properties();
        try {
            sprop.load(new FileInputStream(configurationFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String workMode = sprop.getProperty("app.service.workmode");
        String baseURI = sprop.getProperty("api.base-uri");
        String filePath = sprop.getProperty("file.path");

        if (workMode.equals("api")) {
            System.out.println("Starting online (API) contact manager...");
            ObjectMapper mapper = new ObjectMapper();
            HttpClient client = HttpClient.newBuilder().build();
            usersService = new ApiUsersService(baseURI, mapper, client);
            contactsService = new ApiContactsService(usersService, mapper, client, baseURI);
        } else if (workMode.equals("file")) {
            System.out.println("Starting NIO file contact manager...");
            ContactSerializer contactSerializer = new DefaultContactSerializer();
            usersService = new DummyUsersService();
            contactsService = new NioFileContactsService(contactSerializer, filePath);
        } else if (workMode.equals("memory")) {
            System.out.println("Starting in memory (RAM) contact manager...");
            usersService = new DummyUsersService();
            contactsService = new InMemoryContactsService();
        } else {
            throw new RuntimeException(workMode+" is not supported work mode!");
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















