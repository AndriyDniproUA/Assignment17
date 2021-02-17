package com.savytskyy.contactservices.factories;

import com.savytskyy.contactservices.services.usersservice.UsersService;

import java.net.http.HttpRequest;

public interface HttpRequestCreator {
    public HttpRequest createAuthorizedGetRequest(String url);
    public HttpRequest createAuthorizedPostRequest(String url, Object obj);
    public HttpRequest createGetRequest(String url);
    public HttpRequest createPostRequest(String url, Object obj);


}
