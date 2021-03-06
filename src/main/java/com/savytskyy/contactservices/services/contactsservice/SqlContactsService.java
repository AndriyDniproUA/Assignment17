package com.savytskyy.contactservices.services.contactsservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.dto.GetStatusResponse;
import com.savytskyy.contactservices.dto.contacts.AddContactRequest;
import com.savytskyy.contactservices.dto.contacts.FindContactByNameRequest;
import com.savytskyy.contactservices.dto.contacts.FindContactByValueRequest;
import com.savytskyy.contactservices.dto.contacts.GetContactsResponse;
import com.savytskyy.contactservices.entities.Contact;
import com.savytskyy.contactservices.factories.HttpRequestCreator;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SqlContactsService implements ContactsService {
    private final UsersService usersService;
    private final DataSource dataSource;

    @Override
    public void add(Contact contact) {
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO contactlist" +
                            "(contact_owner, contact_name, contact_type, contact_value) VALUES (?,?,?,?)");

            statement.setInt(1, Integer.valueOf(usersService.getToken()));
            statement.setString(2, contact.getName());
            statement.setString(3, contact.getType().toString().toLowerCase());
            statement.setString(4, contact.getValue());

            statement.execute();

            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void remove(Integer id) {
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement("DELETE FROM contactlist WHERE (contact_id=? AND contact_owner=?)");
            statement.setInt(1, id);
            statement.setInt(2, Integer.valueOf(usersService.getToken()));

            statement.execute();

            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<Contact> findByName(String name) {
        List<Contact> contacts = new ArrayList();

        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection
                    .prepareStatement("SELECT contact_id, contact_name, contact_type, contact_value " +
                            "FROM contactlist WHERE (contact_name LIKE ? AND contact_owner=?)");
            statement.setString(1, "%" + name + "%");
            statement.setInt(2, Integer.valueOf(usersService.getToken()));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Contact contact = new Contact();
                contact.setId(resultSet.getInt("contact_id"));
                contact.setName(resultSet.getString("contact_name"));
                contact.setType(Contact.ContactType
                        .valueOf(resultSet.getString("contact_type").toUpperCase()));
                contact.setValue(resultSet.getString("contact_value"));
                contacts.add(contact);
            }
            connection.close();

            return contacts;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Contact> findByValue(String value) {
        List<Contact> contacts = new ArrayList();

        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection
                    .prepareStatement("SELECT contact_id, contact_name, contact_type, contact_value " +
                            "FROM contactlist WHERE (contact_value LIKE ? AND contact_owner=?)");
            statement.setString(1, "%" + value + "%");
            statement.setInt(2, Integer.valueOf(usersService.getToken()));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Contact contact = new Contact();
                contact.setId(resultSet.getInt("contact_id"));
                contact.setName(resultSet.getString("contact_name"));
                contact.setType(Contact.ContactType
                        .valueOf(resultSet.getString("contact_type").toUpperCase()));
                contact.setValue(resultSet.getString("contact_value"));
                contacts.add(contact);
            }
            connection.close();

            return contacts;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Contact> getAll() {
        List<Contact> contacts = new ArrayList();

        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection
                    .prepareStatement
                            ("SELECT contact_id, CONTACT_OWNER, contact_name," +
                                    " contact_type, contact_value FROM contactlist WHERE contact_owner=?");
            statement.setInt(1, Integer.valueOf(usersService.getToken()));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Contact contact = new Contact();
                contact.setId(resultSet.getInt("contact_id"));
                contact.setName(resultSet.getString("contact_name"));
                contact.setType(Contact.ContactType
                        .valueOf(resultSet.getString("contact_type").toUpperCase()));
                contact.setValue(resultSet.getString("contact_value"));
                contacts.add(contact);
            }
            connection.close();

            return contacts;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Collections.emptyList();
    }
}
