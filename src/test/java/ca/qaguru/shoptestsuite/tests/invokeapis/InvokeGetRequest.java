package ca.qaguru.shoptestsuite.tests.invokeapis;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertEquals;

public class InvokeGetRequest {
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
                .when()
                .get("41f43b3a-5143-4016-840b-3bfae9bce279")
                .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .body("name", is("My Waterbottle"));
    }
}
