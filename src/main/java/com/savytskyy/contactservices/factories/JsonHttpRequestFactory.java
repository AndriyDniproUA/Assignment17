package com.savytskyy.contactservices.factories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.savytskyy.contactservices.services.usersservice.UsersService;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.net.http.HttpRequest;

@RequiredArgsConstructor
public class JsonHttpRequestFactory implements HttpRequestCreator {
    private final UsersService usersService;
    private final ObjectMapper mapper;
    private final String baseURI;

    @Override
    public HttpRequest createAuthorizedGetRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseURI + url))
                .GET()
                .header("Authorization", "Bearer " + usersService.getToken())
                .header("Content-Type", "application/json")
                .build();
    }

    @Override
    public HttpRequest createAuthorizedPostRequest(String url, Object request) {
        try {
            return HttpRequest.newBuilder()
                    .uri(URI.create(baseURI + url))
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(request)))
                    .header("Authorization", "Bearer " + usersService.getToken())
                    .header("Content-Type", "application/json")
                    .build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
