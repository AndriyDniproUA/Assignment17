package com.savytskyy.contactservices.factories;

import java.net.http.HttpRequest;

public interface HttpRequestCreator {
    public HttpRequest createAuthorizedGetRequest(String url);
    public HttpRequest createAuthorizedPostRequest(String url, Object obj);
}
