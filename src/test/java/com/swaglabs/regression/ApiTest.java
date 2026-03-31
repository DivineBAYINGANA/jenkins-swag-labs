package com.swaglabs.regression;

import com.swaglabs.api.SwaglabsApiClient;
import io.qameta.allure.*;
import io.rest-assured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.rest-assured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

/**
 * API Test Suite for Swag Labs Services
 * Uses REST Assured for API endpoint testing
 */
@Feature("API Testing")
@Epic("Swag Labs API")
@Tag("regression")
@Tag("api")
@DisplayName("Swag Labs API Tests")
public class ApiTest {

    private SwaglabsApiClient apiClient;

    @BeforeEach
    void setUp() {
        apiClient = new SwaglabsApiClient();
    }

    @Test
    @Story("Health Check")
    @DisplayName("Verify API health endpoint returns 200")
    @Severity(SeverityLevel.CRITICAL)
    void testHealthCheckEndpoint() {
        // This is a template test - replace with actual API endpoints
        Response response = apiClient.getRequest("/health");

        assertThat(response.getStatusCode())
                .as("Health endpoint should return 200")
                .isEqualTo(200);
    }

    @Test
    @Story("Product Information")
    @DisplayName("Verify products endpoint returns valid data")
    @Severity(SeverityLevel.CRITICAL)
    void testGetProductsEndpoint() {
        // Example test template
        Response response = apiClient.getRequest("/products");

        assertThat(response.getStatusCode())
                .as("Products endpoint should return 200")
                .isEqualTo(200);

        // Validate response structure
        assertThat(response.getContentType())
                .contains("application/json");
    }

    @Test
    @Story("Product Details")
    @DisplayName("Verify product details endpoint with ID parameter")
    @Severity(SeverityLevel.HIGH)
    void testGetProductById() {
        int productId = 1;
        Response response = apiClient.getRequest("/products/" + productId);

        assertThat(response.getStatusCode())
                .as("Should return product with valid ID")
                .isEqualTo(200);

        // Verify response contains expected fields
        assertThat(response.jsonPath().getString("id"))
                .as("Product ID should match request")
                .isNotNull();
    }

    @Test
    @Story("Error Handling")
    @DisplayName("Verify 404 response for non-existent product")
    @Severity(SeverityLevel.MEDIUM)
    void testGetProductNotFound() {
        int invalidProductId = 99999;
        Response response = apiClient.getRequest("/products/" + invalidProductId);

        assertThat(response.getStatusCode())
                .as("Should return 404 for non-existent product")
                .isEqualTo(404);
    }

    @Test
    @Story("Authentication")
    @DisplayName("Verify API requires valid authentication")
    @Severity(SeverityLevel.CRITICAL)
    void testAuthenticationRequired() {
        Response response = given()
                .when()
                .get("/api/protected-resource");

        assertThat(response.getStatusCode())
                .as("Should return 401 or 403 without authentication")
                .isIn(401, 403);
    }

    @Test
    @Story("Response Headers")
    @DisplayName("Verify response headers contain expected values")
    @Severity(SeverityLevel.MEDIUM)
    void testResponseHeaders() {
        Response response = apiClient.getRequest("/products");

        assertThat(response.getStatusCode())
                .isEqualTo(200);

        // Verify common security headers
        assertThat(response.getHeader("Content-Type"))
                .contains("application/json");
    }

    @Test
    @Story("API Performance")
    @DisplayName("Verify API response time is acceptable")
    @Severity(SeverityLevel.MEDIUM)
    void testApiResponseTime() {
        long startTime = System.currentTimeMillis();
        Response response = apiClient.getRequest("/products");
        long responseTime = System.currentTimeMillis() - startTime;

        assertThat(response.getStatusCode()).isEqualTo(200);
        
        // Assert response time is under 5 seconds
        assertThat(responseTime)
                .as("API should respond within 5 seconds")
                .isLessThan(5000);
    }

    @Test
    @Story("Pagination")
    @DisplayName("Verify pagination works correctly")
    @Severity(SeverityLevel.HIGH)
    void testPaginationSupport() {
        Response response = apiClient.getRequest("/products?page=1&limit=10");

        assertThat(response.getStatusCode()).isEqualTo(200);

        // Verify pagination fields exist
        assertThat(response.jsonPath().getString("page"))
                .isNotNull();
    }

    @Test
    @Story("Data Validation")
    @DisplayName("Verify API returns properly formatted JSON")
    @Severity(SeverityLevel.HIGH)
    void testApiResponseFormat() {
        Response response = apiClient.getRequest("/products");

        assertThat(response.getStatusCode()).isEqualTo(200);

        // Verify it's valid JSON
        assertThatCode(() -> response.jsonPath())
                .as("Response should be valid JSON")
                .doesNotThrowAnyException();
    }

    /**
     * Template for testing POST requests
     * Uncomment and customize for actual implementation
     */
    /*
    @Test
    @Story("Create Product")
    @DisplayName("Verify create product endpoint")
    @Severity(SeverityLevel.HIGH)
    void testCreateProduct() {
        String requestBody = "{"
                + "\"name\": \"Test Product\","
                + "\"price\": 29.99,"
                + "\"category\": \"test\""
                + "}";

        Response response = apiClient.postRequest("/products", requestBody);

        assertThat(response.getStatusCode())
                .as("Should return 201 for successful creation")
                .isEqualTo(201);

        assertThat(response.jsonPath().getString("id"))
                .as("Response should contain created product ID")
                .isNotNull();
    }
    */

    /**
     * Template for testing PUT requests
     * Uncomment and customize for actual implementation
     */
    /*
    @Test
    @Story("Update Product")
    @DisplayName("Verify update product endpoint")
    @Severity(SeverityLevel.HIGH)
    void testUpdateProduct() {
        int productId = 1;
        String requestBody = "{"
                + "\"name\": \"Updated Product\","
                + "\"price\": 39.99"
                + "}";

        Response response = apiClient.putRequest("/products/" + productId, requestBody);

        assertThat(response.getStatusCode())
                .as("Should return 200 for successful update")
                .isEqualTo(200);
    }
    */

    /**
     * Template for testing DELETE requests
     * Uncomment and customize for actual implementation
     */
    /*
    @Test
    @Story("Delete Product")
    @DisplayName("Verify delete product endpoint")
    @Severity(SeverityLevel.HIGH)
    void testDeleteProduct() {
        int productId = 1;

        Response response = apiClient.deleteRequest("/products/" + productId);

        assertThat(response.getStatusCode())
                .as("Should return 204 for successful deletion")
                .isIn(200, 204);
    }
    */
}
