package com.savytskyy.contactservices.services.usersservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.dto.GetStatusResponse;
import com.savytskyy.contactservices.dto.contacts.GetContactsResponse;
import com.savytskyy.contactservices.dto.users.GetUsersResponse;
import com.savytskyy.contactservices.dto.users.LoginRequest;
import com.savytskyy.contactservices.dto.users.LoginResponse;
import com.savytskyy.contactservices.dto.users.RegistrationRequest;
import com.savytskyy.contactservices.entities.Contact;
import com.savytskyy.contactservices.entities.User;
import com.savytskyy.contactservices.factories.HttpRequestCreator;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ApiUsersService implements UsersService {
    private final String baseURI;
    private final ObjectMapper mapper;
    private final HttpClient client;
    private final HttpRequestCreator httpRequestFactory;

    private String token;
    private LocalDateTime ldt;
    private List<User> users;


    @Override
    public String getToken() {
        return token;
    }

    @Override
    public boolean isAuth() {
        if (token == null || ldt == null) return false;
        return (Duration.between(ldt, LocalDateTime.now()).toMinutes() < 55);
    }

    @Override
    public void register(User user) {
        RegistrationRequest regRequest = new RegistrationRequest();
        regRequest.setLogin(user.getLogin());
        regRequest.setPassword(user.getPassword());
        regRequest.setDateBorn(user.getDateOfBirth().toString());
        try {
            HttpRequest httpRequest = httpRequestFactory.createPostRequest("/register", regRequest);
            //HttpRequest httpRequest = createHttpRequest("/register", regRequest);
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            GetStatusResponse response = mapper.readValue(
                    httpResponse.body(),
                    GetStatusResponse.class);

            if (!response.getStatus().equals("ok")) {
                //throw new RuntimeException(response.getError());
                System.out.println("Registration failed: " + response.getError());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void login(User user) {

        LoginRequest logRequest = new LoginRequest();
        logRequest.setLogin(user.getLogin());
        logRequest.setPassword(user.getPassword());

        try {

            HttpRequest httpRequest = httpRequestFactory.createPostRequest("/login", logRequest);
            //HttpRequest httpRequest = createHttpRequest("/login", logRequest);


           HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            LoginResponse response = mapper.readValue(
                    httpResponse.body(),
                    LoginResponse.class);


            if (!response.getStatus().equals("ok")) {
                //throw new RuntimeException(response.getError());
                System.out.println("Login failed: "+response.getError());
            }
            token = response.getToken();
            ldt = LocalDateTime.now();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAll() {
        HttpRequest httpRequest = httpRequestFactory.createGetRequest("/users");
//        HttpRequest httpRequest = HttpRequest.newBuilder()
//                .uri(URI.create(baseURI + "/users"))
//                .GET()
//                .header("Content-Type", "application/json")
//                .build();

        try {
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            GetUsersResponse response = mapper.readValue(httpResponse.body(), GetUsersResponse.class);

            if (!response.getStatus().equals("ok")) {
                throw new RuntimeException("Users not found!");
            }
            return response.getUsers().stream().map(u -> {
                User user = new User();
                user.setLogin(u.getLogin());
                user.setDateOfBirth(LocalDate.parse(u.getDateBorn()));
                return user;
            }).collect(Collectors.toList());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

//    private HttpRequest createHttpRequest(String uri, Object request) throws JsonProcessingException {
//        return HttpRequest.newBuilder()
//                .uri(URI.create(baseURI + uri))
//                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(request)))
//                .header("Content-Type", "application/json")
//                .header("Accept","application/json")
//                .build();
//    }
}


