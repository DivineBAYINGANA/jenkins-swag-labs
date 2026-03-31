package com.swaglabs.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class SwaglabsApiClient {

    private static final String BASE_URL = System.getProperty("api.baseUrl", "https://restapi.example.com");

    static {
        RestAssured.baseURI = BASE_URL;
        RestAssured.basePath = "/api";
    }

    public static RequestSpecification getRequestSpec() {
        return given()
                .contentType("application/json")
                .accept("application/json")
                .header("User-Agent", "RestAssured-Test-Client");
    }

    public static Response getRequest(String endpoint) {
        return getRequestSpec().get(endpoint);
    }

    public static Response postRequest(String endpoint, String body) {
        return getRequestSpec().body(body).post(endpoint);
    }

    public static Response putRequest(String endpoint, String body) {
        return getRequestSpec().body(body).put(endpoint);
    }

    public static Response deleteRequest(String endpoint) {
        return getRequestSpec().delete(endpoint);
    }

    public static String getResponseBody(Response response) {
        return response.getBody().asString();
    }

    public static int getStatusCode(Response response) {
        return response.getStatusCode();
    }

    public static String getHeader(Response response, String headerName) {
        return response.getHeader(headerName);
    }
}
