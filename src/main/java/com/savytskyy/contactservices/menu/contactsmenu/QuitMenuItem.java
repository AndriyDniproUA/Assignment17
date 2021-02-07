package com.savytskyy.contactservices.menu.contactsmenu;

import com.savytskyy.contactservices.menu.MenuItem;

public class QuitMenuItem implements MenuItem {

    @Override
    public void doAction() {
        System.out.println("Good Bye!");
        System.out.println("Thank you for using our service!");
    }

    @Override
    public String getName() {
        return "Quit menu";
    }

    @Override
    public boolean closeAfter() {
        return true;
    }

    @Override
    public boolean isVisible(){
        return true;
    }



}
