package com.swaglabs.api;

import io.rest-assured.RestAssured;
import io.rest-assured.response.Response;
import io.rest-assured.specification.RequestSpecification;

import static io.rest-assured.RestAssured.*;

/**
 * SwaglabsApiClient - REST Assured wrapper for Swag Labs API
 * Responsibilities:
 *   - Configure base URL and common request headers
 *   - Provide helper methods for API operations (GET, POST, PUT, DELETE)
 *   - Handle response validation and error handling
 */
public class SwaglabsApiClient {

    private static final String BASE_URL = System.getProperty("api.baseUrl", "https://restapi.example.com");
    private static final String CONTENT_TYPE = "application/json";

    /**
     * Initialize REST Assured configuration
     */
    static {
        RestAssured.baseURI = BASE_URL;
        RestAssured.basePath = "/api";
    }

    /**
     * Get a common request specification with default headers
     */
    public static RequestSpecification getRequestSpec() {
        return given()
                .contentType(CONTENT_TYPE)
                .accept(CONTENT_TYPE)
                .header("User-Agent", "RestAssured-Test-Client");
    }

    /**
     * GET request to specified endpoint
     */
    public static Response getRequest(String endpoint) {
        return getRequestSpec()
                .get(endpoint);
    }

    /**
     * GET request with query parameters
     */
    public static Response getRequest(String endpoint, QueryParams params) {
        RequestSpecification spec = getRequestSpec();
        if (params != null) {
            params.getParams().forEach(spec::queryParam);
        }
        return spec.get(endpoint);
    }

    /**
     * POST request with JSON body
     */
    public static Response postRequest(String endpoint, String body) {
        return getRequestSpec()
                .body(body)
                .post(endpoint);
    }

    /**
     * POST request with object body
     */
    public static Response postRequest(String endpoint, Object body) {
        return getRequestSpec()
                .body(body)
                .post(endpoint);
    }

    /**
     * PUT request with JSON body
     */
    public static Response putRequest(String endpoint, String body) {
        return getRequestSpec()
                .body(body)
                .put(endpoint);
    }

    /**
     * DELETE request
     */
    public static Response deleteRequest(String endpoint) {
        return getRequestSpec()
                .delete(endpoint);
    }

    /**
     * Get response body as string
     */
    public static String getResponseBody(Response response) {
        return response.getBody().asString();
    }

    /**
     * Get response status code
     */
    public static int getStatusCode(Response response) {
        return response.getStatusCode();
    }

    /**
     * Get response header value
     */
    public static String getHeader(Response response, String headerName) {
        return response.getHeader(headerName);
    }

    /**
     * Helper class for building query parameters
     */
    public static class QueryParams {
        private final java.util.Map<String, String> params = new java.util.HashMap<>();

        public QueryParams add(String key, String value) {
            params.put(key, value);
            return this;
        }

        public java.util.Map<String, String> getParams() {
            return params;
        }
    }
}
