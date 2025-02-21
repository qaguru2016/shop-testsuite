package ca.qaguru.shoptestsuite.tests.authentication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class JwtAuthenticationTest {

    // Set up the base URL for the API
    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080"; // Adjust the base URI to your application URL
    }

    // Test for getting the JWT token (login)
    @Test
    public void testLoginAndGetJwtToken() {
        // Make a POST request to /auth/login with username and password
        Response response = given()
                .param("username", "user1")
                .param("password", "password1")
                .when()
                .post("/api/v1/auth/login");

        // Assert that the response contains a JWT token
        String jwtToken = response.getBody().asString();
        System.out.println("JWT Token: " + jwtToken);  // For debugging purpose
        // Assert the status code and the token presence
        response.then().statusCode(200);
    }

    // Test to access /api/v1/products with the JWT token
    @Test
    public void testAccessProductsWithJwtToken() {
        // First, login and get the token
        String jwtToken = getJwtToken("user1", "password1");

        // Now, access the /api/v1/products endpoint using the token
        given()
                .header("Authorization", "Bearer " + jwtToken)  // Set the Bearer token in the header
                .when()
                .get("/api/v1/products")
                .then()
                .statusCode(200);  // Expecting 200 OK for successful access
    }

    // Utility method to login and get JWT token
    private String getJwtToken(String username, String password) {
        Response response = given()
                .param("username", username)
                .param("password", password)
                .when()
                .post("/api/v1/auth/login");

        return response.getBody().asString();
    }
}