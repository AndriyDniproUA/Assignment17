package com.savytskyy.contactservices.menu;

public interface MenuItem {
    void doAction();
    String getName();
    boolean closeAfter();
    boolean isVisible();
}
