package ca.qaguru.shoptestsuite.tests.payloads;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

public class PayLoadWithFile {
    @Test
    public void saveAProduct() {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL).setBaseUri("http://localhost:8080")
                .setBasePath("api/v1/products").setContentType(ContentType.JSON).build();
        File jsonFile = new File("src/test/resources/product.json");
        given()
                .spec(requestSpecification)
                .body(jsonFile)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(HttpStatus.SC_CREATED)
                .header("Location",containsString("/api/v1/products/"));
    }
}
