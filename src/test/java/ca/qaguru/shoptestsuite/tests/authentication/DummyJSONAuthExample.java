package ca.qaguru.shoptestsuite.tests.authentication;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;


public class DummyJSONAuthExample {
    @Test
    public void authTest() {
        // Base URI for DummyJSON
        RestAssured.baseURI = "https://dummyjson.com";

        // Login and get the Bearer token
        JsonObject requestParams = new JsonObject();
        requestParams.addProperty("username", "michaelw");
        requestParams.addProperty("password", "michaelwpass");

        Response loginResponse = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(requestParams.toString())
                .post("/auth/login");

        loginResponse.then().statusCode(200);

        // Extract token
        String token = loginResponse.jsonPath().getString("accessToken");
        System.out.println("Token: " + token);

        // Step 3: Use the token to access a protected endpoint
        Response userResponse = RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .get("/auth/users");

        userResponse.then().statusCode(200);
        System.out.println("Response: " + userResponse.getBody().prettyPrint());
    }
}
