package com.savytskyy.contactservices.menu.usersmenu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.entities.User;
import com.savytskyy.contactservices.menu.MenuItem;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpClient;
import java.time.LocalDate;
import java.util.Scanner;

@RequiredArgsConstructor
public class RegisterUserMenuItem implements MenuItem {
    private final UsersService usersService;

    Scanner sc = new Scanner(System.in);

    @Override
    public void doAction() {
        System.out.print("Please enter your login: ");
        String login = sc.nextLine();
        System.out.print("Please enter your password: ");
        String password = sc.nextLine();
        System.out.print("Please enter your birth date [YYYY-MM-dd]: ");
        String dateBorn = sc.nextLine();

        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setDateOfBirth(LocalDate.parse(dateBorn));

        usersService.register(user);
    }

    @Override
    public String getName() {
        return "Register a new user";
    }

    @Override
    public boolean closeAfter() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return !usersService.isAuth();
    }




}
