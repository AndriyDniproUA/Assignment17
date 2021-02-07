package com.savytskyy.contactservices.services.contactsservice;

import com.savytskyy.contactservices.entities.Contact;
import com.savytskyy.contactservices.utils.ContactSerializer;
import com.savytskyy.contactservices.utils.NioFileUtil;
import lombok.RequiredArgsConstructor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class NioFileContactsService implements ContactsService {
    private final ContactSerializer contactSerializer;
    private final String filePath;


    @Override
    public void add(Contact contact) {
        contact.setId(newID());
        System.out.println(contact.getId());

        try (FileChannel channel = (FileChannel) Files.newByteChannel(Path.of(filePath),
                StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
            ByteBuffer buffer = ByteBuffer.allocate(100);
            buffer.clear();
            buffer.put((contactSerializer.serialize(contact)+ "\n").getBytes(StandardCharsets.UTF_8));
            buffer.limit(buffer.position());
            buffer.position(0);
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void remove(Integer id) {
        List<Contact> contacts = getAll().stream()
                .filter(c -> c.getId() != id)
                .collect(Collectors.toList());

        try (FileChannel channel = (FileChannel) Files.newByteChannel(Path.of(filePath),
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            ByteBuffer buffer = ByteBuffer.allocate(50);

            for (Contact contact : contacts) {
                buffer.clear();
                buffer.put((contactSerializer.serialize(contact) + "\n").getBytes(StandardCharsets.UTF_8));
                buffer.limit(buffer.position());
                buffer.position(0);
                channel.write(buffer);
            }
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
        NioFileUtil fileUtil = new NioFileUtil(Path.of(filePath));

        fileUtil.readByLine(new Consumer<String>() {
            @Override
            public void accept(String s) {
                Contact contact = contactSerializer.deserialize(s);
                if (predicate.test(contact)) {
                    contacts.add(contact);
                }
            }
        });
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




