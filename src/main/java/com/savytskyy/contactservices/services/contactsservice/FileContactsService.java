package com.savytskyy.contactservices.services.contactsservice;

import com.savytskyy.contactservices.entities.Contact;
import com.savytskyy.contactservices.utils.ContactSerializer;
import lombok.RequiredArgsConstructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FileContactsService implements ContactsService {
    private final ContactSerializer contactSerializer;
    private final String filePath;


    @Override
    public void add(Contact contact) {
        contact.setId(newID());

        try (FileOutputStream fos = new FileOutputStream(filePath, true)) {
            fos.write((contactSerializer.serialize(contact) + "\n").getBytes(StandardCharsets.UTF_8));
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Integer id) {
        List<Contact> contacts = getAll().stream()
                .filter(c -> c.getId() != id)
                .collect(Collectors.toList());

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            for (Contact contact : contacts) {
                fos.write((contactSerializer.serialize(contact) + "\n").getBytes(StandardCharsets.UTF_8));
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Contact> findByName(String name) {
        return readContacts(c -> c.getName().startsWith(name));
    }

    @Override
    public List<Contact> findByValue(String value) {
        return readContacts(c -> c.getValue().contains(value));
    }

    @Override
    public List<Contact> getAll() {
        return readContacts(c -> true);
    }

    private List<Contact> readContacts(Predicate<Contact> predicate) {
        List<Contact> contacts = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath)) {
            Scanner scanner = new Scanner(fis);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isBlank()) continue;
                Contact contact = contactSerializer.deserialize(line);
                if (predicate.test(contact)) {
                    contacts.add(contact);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contacts;
    }
    private int newID() {
        return getAll().stream()
                .map(c -> c.getId())
                .max(Comparator.comparingInt(a -> a))
                .map(id -> id + 1)
                .orElse(1);
    }


}
