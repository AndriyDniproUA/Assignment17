package com.savytskyy.contactservices.utils;

import com.savytskyy.contactservices.entities.Contact;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultContactSerializer implements ContactSerializer {

    //private final static String PATTERN = "(\\d+)\\[(\\w+):([a-zA-Zа-яА-Я.\\d]+.+?)\\|(.+)\\]";

    @Override
    public String serialize(Contact c) {
        return String.format("%d[%s:%s|%s]",
                c.getId(),
                c.getType().toString().toLowerCase(),
                c.getName(),
                c.getValue());
    }

    @Override
    public Contact deserialize(String s) {
        String[] parts = s.split("\\[|:|\\||\\]");

        String id = parts[0];
        String type = parts[1];
        String name = parts[2];
        String value = parts[3];

        Contact contact = new Contact();
        contact.setId(Integer.parseInt(id));
        contact.setType(Contact.ContactType.valueOf(type.toUpperCase()));
        contact.setName(name);
        contact.setValue(value);

        return contact;

//********** ПРИ РАБОТЕ С КИРИЛЛИЦЕЙ ПЕРИОДИЧЕСКИ "ПАДАЕТ" ЧТЕНИЕМ ИЗ ФАЙЛА ЗНАКОВ (?)
//*********** ПРИ ЭТОМ, В ФАЙЛЕ ВСЕ НОРМАЛЬНО !

        //Pattern pattern = Pattern.compile(PATTERN);
        //Matcher matcher = pattern.matcher(s);

//        if (matcher.matches())  {
//            String id = matcher.group(1);
//            String type = matcher.group(2);
//            String name = matcher.group(3);
//            String value = matcher.group(4);
//
//
//
//            return contact;
//        };
        //throw new RuntimeException("Invalid contact data in file");

    }
}
