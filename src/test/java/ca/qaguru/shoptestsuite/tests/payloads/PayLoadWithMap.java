package ca.qaguru.shoptestsuite.tests.payloads;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

public class PayLoadWithMap {
    @Test
    public void saveAProduct() {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL).setBaseUri("http://localhost:8080")
                .setBasePath("api/v1/products").setContentType(ContentType.JSON).build();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Fusion Backpack");
        requestBody.put("description", "Comfy travel bag");
        requestBody.put("price", 80F);
        given()
                .spec(requestSpecification)
                .body(requestBody)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(HttpStatus.SC_CREATED)
                .header("Location",containsString("/api/v1/products/"));
    }
}
