package ca.qaguru.shoptestsuite.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class PathParameter {
    @Test
    public void getAProduct() {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setBaseUri("http://localhost:8080")
                .setBasePath("api/v1/products")
                .setContentType(ContentType.JSON)
                .build();
        given()
                .spec(requestSpecification)
                .pathParam("productId","03b8b7d3-3c9b-4e8c-b59d-edaac1d3d602")
                .when()
                .get("{productId}")
                .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("name", is("Samsung Soundbar"));
    }
}
