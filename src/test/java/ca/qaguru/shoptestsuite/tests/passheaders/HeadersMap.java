package ca.qaguru.shoptestsuite.tests.passheaders;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

public class HeadersMap {
    @Test
    public void saveAProduct() {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setBaseUri("http://localhost:8080")
                .setBasePath("api/v1/products")
                .build();
        String payload = "{\"name\": \"Fusion Backpack\", \"description\": \"Comfy travel bag\", \"price\": 80}";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer your_token_here");
        headers.put("Content-Type", "application/json");
        given()
                .spec(requestSpecification)
                .headers(headers)
                .body(payload)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(HttpStatus.SC_CREATED)
                .header("Location",containsString("/api/v1/products/"));
    }
}
