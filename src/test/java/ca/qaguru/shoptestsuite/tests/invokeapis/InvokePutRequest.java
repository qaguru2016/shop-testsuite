package ca.qaguru.shoptestsuite.tests.invokeapis;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class InvokePutRequest {
    @Test
    public void updateAProduct() {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setBaseUri("http://localhost:8080")
                .setBasePath("api/v1/products")
                .setContentType(ContentType.JSON)
                .build();
        String payload = "{\"name\": \"Fashion Backpack\", \"description\": \"Executive bag\", \"price\": 100}";
        given()
                .spec(requestSpecification)
                .body(payload)
                .when()
                .put("/41f43b3a-5143-4016-840b-3bfae9bce279")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
