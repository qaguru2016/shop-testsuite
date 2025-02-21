package ca.qaguru.shoptestsuite.tests.invokeapis;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.is;

public class FirstTest {
    @Test
    public void getAProduct() {
        get("http://localhost:8080/api/v1/products/41f43b3a-5143-4016-840b-3bfae9bce279")
                .then()
                .statusCode(200)
                .assertThat()
                .body("name", is("My Waterbottle"));
    }
}
