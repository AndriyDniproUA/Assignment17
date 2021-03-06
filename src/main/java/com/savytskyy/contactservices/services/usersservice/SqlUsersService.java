package com.savytskyy.contactservices.services.usersservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.dto.GetStatusResponse;
import com.savytskyy.contactservices.dto.users.GetUsersResponse;
import com.savytskyy.contactservices.dto.users.LoginRequest;
import com.savytskyy.contactservices.dto.users.LoginResponse;
import com.savytskyy.contactservices.dto.users.RegistrationRequest;
import com.savytskyy.contactservices.entities.User;
import com.savytskyy.contactservices.factories.HttpRequestCreator;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SqlUsersService implements UsersService {
    private final DataSource dataSource;

    private String token = null;
    //    private LocalDateTime ldt;
    private List<User> users;


    @Override
    public String getToken() {
        return token;
    }

    @Override
    public boolean isAuth() {
        return (token != null);
    }

    @Override
    public void register(User user) {
        Boolean userAlreadyExists = false;

        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection
                    .prepareStatement("SELECT user_login FROM users WHERE user_login=?");
            statement.setString(1, user.getLogin());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) userAlreadyExists = true;
            if (userAlreadyExists) {
                System.out.println("Registration failed!");
                System.out.println("Login " + user.getLogin() + " is already taken!");
            } else {
                statement = connection.prepareStatement(
                        "INSERT INTO users (user_login, user_password, user_dateofbirth) VALUES (?,?,?)");
                statement.setString(1, user.getLogin());
                statement.setString(2, user.getPassword());
                statement.setDate(3, Date.valueOf(user.getDateOfBirth()));

                statement.execute();
            }

            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void login(User user) {
        String password = null;
        Integer user_id = 0;

        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection
                    .prepareStatement("SELECT user_login, user_password FROM users WHERE user_login=?");
            statement.setString(1, user.getLogin());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                password = resultSet.getString("user_password");
                if (password.equals(user.getPassword())) {
                    statement = connection.prepareStatement(
                            "SELECT user_id FROM users WHERE user_login=?");
                    statement.setString(1, user.getLogin());
                    resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        user_id = resultSet.getInt("user_id");
                    }
                    token = user_id.toString();
                }
            } else {
                System.out.println("Sorry!");
                System.out.println("Contact not found!");
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<User> getAll() {
        users = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection
                    .prepareStatement("SELECT user_login, user_dateofbirth FROM users");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setLogin(resultSet.getString("user_login"));
                user.setDateOfBirth(LocalDate.parse(resultSet.getString("user_dateofbirth")));
                users.add(user);
            }
            connection.close();
            return users;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Collections.emptyList();
    }
}


