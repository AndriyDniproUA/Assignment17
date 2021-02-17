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
    public HttpRequest createAuthorizedPostRequest(String url, Object obj) {
        try {
            return HttpRequest.newBuilder()
                    .uri(URI.create(baseURI + url))
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(obj)))
                    .header("Authorization", "Bearer " + usersService.getToken())
                    .header("Content-Type", "application/json")
                    .build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HttpRequest createGetRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseURI + url))
                .GET()
                .header("Content-Type", "application/json")
                .build();
    }

    @Override
    public HttpRequest createPostRequest(String url, Object obj) {
        try {
            return HttpRequest.newBuilder()
                    .uri(URI.create(baseURI + url))
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(obj)))
                                        .header("Content-Type", "application/json")
                    .build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
