package com.savytskyy.contactservices.menu.usersmenu;

import com.savytskyy.contactservices.entities.User;
import com.savytskyy.contactservices.menu.MenuItem;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import lombok.RequiredArgsConstructor;
import java.util.Scanner;


@RequiredArgsConstructor
public class LoginUserMenuItem implements MenuItem {
    private final UsersService usersService;

    Scanner sc = new Scanner(System.in);

    @Override
    public void doAction() {

        System.out.print("Please enter your login: ");
        String login = sc.nextLine();
        System.out.print("Please enter your password: ");
        String password = sc.nextLine();

        User user = new User();
        user.setLogin(login);
        user.setPassword(password);

        usersService.login(user);
    }

    @Override
    public String getName() {
        return "Login existing user";
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