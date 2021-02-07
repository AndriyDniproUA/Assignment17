package com.savytskyy.contactservices;


import com.savytskyy.contactservices.menu.MenuItem;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class Menu {
    private Scanner sc = new Scanner(System.in);
    public List<MenuItem> menuItems = new ArrayList<>();

    public void run() {
        while (true) {
            System.out.println("---------------------------------");
            System.out.println("Please choose menu item:");
            showMenu();

            int choice = askUserChoice();
            int index = choice - 1;
            if (indexIsValid(index)) {
                menuItems.get(index).doAction();
            } else {
                System.out.printf("Please enter the number within 1-%d range\n",
                        menuItems.size());
                continue;
            }
            if (menuItems.get(index).closeAfter()) break;
        }
    }


    public void addMenuItem(MenuItem item) {
        menuItems.add(item);
    }


    public void showMenu() {
        System.out.println("--------------MENU ----------------");
        for (int i = 0; i < menuItems.size(); i++) {
            if (menuItems.get(i).isVisible()) {
                System.out.printf("[%d] - %s \n",
                        i + 1, menuItems.get(i).getName());
            }
        }
        System.out.println("---------------------------------");
    }

    private int askUserChoice() {
        System.out.println("Please enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
        return choice;
    }

    private boolean indexIsValid(int index) {
        return index >= 0 && index < menuItems.size();
    }
}
