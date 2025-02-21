package ca.qaguru.shoptestsuite.tests.payloads;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;

public class PayLoadWithString {
    @Test
    public void saveAProduct() {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setBaseUri("http://localhost:8080")
                .setBasePath("api/v1/products")
                .setContentType(ContentType.JSON)
                .build();
        String payload = "{\"name\": \"Fusion Backpack\", \"description\": \"Comfy travel bag\", \"price\": 80}";
        given()
                .spec(requestSpecification)
                .body(payload)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(HttpStatus.SC_CREATED)
                .header("Location",containsString("/api/v1/products/"));
    }
}
