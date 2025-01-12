package ca.qaguru.shoptestsuite.services;

import ca.qaguru.shoptestsuite.lib.ServiceBase;
import ca.qaguru.shoptestsuite.models.Product;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpStatus;
//import org.apache.tools.ant.taskdefs.condition.Http;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductService extends ServiceBase {
    private UUID productUuid;

    @Builder
    public ProductService(RequestSpecification requestSpecification, UUID productUuid) {
        super(requestSpecification);
        this.productUuid = productUuid;
    }

    public ProductService saveNewProduct(Product product, int expStatusCode) {

        ValidatableResponse response = given()
                .spec(requestSpecification)
                //.auth().basic(userInfo.getUsername(),userInfo.getPassword())
                .body(product)
                .when()
                .post("/products")
                .then().log().all()
                .assertThat().statusCode(expStatusCode);

        if(expStatusCode == HttpStatus.SC_CREATED){
            String location = response.assertThat()
                    .header("Location",containsString("/api/v1/products/"))
                    .extract().header("Location");
            productUuid = UUID.fromString(location.substring("/api/v1/products/".length()));
        }

        return this;

    }

    public ProductService findProductById(UUID uuid,int expStatusCode, Product expProduct) {
        ExtractableResponse<Response> response = given().spec(requestSpecification)
                .when()
                .get("/products/"+uuid)
                .then().log().all()
                .assertThat().statusCode(expStatusCode)
                .extract();
        if(expStatusCode == HttpStatus.SC_OK){
            expProduct.setId(uuid);
            Product actProduct = response.as(Product.class);
            assertEquals(actProduct,expProduct,"product details doesn't match");
        }
        return this;
    }

    public ProductService updateProduct(UUID uuid, Product product, int expStatusCode) {
        given().spec(requestSpecification)
                .body(product)
                .pathParam("productId",uuid)
        .when()
                .put("/products/{productId}")
                .then().log().all()
                .assertThat().statusCode(expStatusCode);
        return this;
    }

    public ProductService deleteProduct(UUID uuid, int expStatusCode) {
        given().spec(requestSpecification)
                .pathParam("productId",uuid)
                .when()
                .delete("/products/{productId}")
                .then().log().all()
                .assertThat().statusCode(expStatusCode);
        return this;

    }

    public ProductService findAllProducts(int expStatusCode, List<Product> expProducts) {
        ExtractableResponse<Response> response = given().spec(requestSpecification)
                .when()
                .get("/products")
                .then().log().all()
                .assertThat().statusCode(expStatusCode)
                .extract();
        if(expStatusCode == HttpStatus.SC_OK){

            List<Product> actProducts = List.of(response.as(Product[].class));
            expProducts.forEach(product -> assertTrue(actProducts.contains(product),"Product not present in the actual list"));
        }
        return this;

    }

    public ProductService setProductIdFor(Product product) {
        product.setId(productUuid);
        return this;
    }


    public void verifyProductInDb(Product expProduct) {
        expProduct.setId(productUuid);
        Product actProduct = getAProductFromDbById();
        assertEquals(actProduct,expProduct,"Values don't match,\nExp : " +
                expProduct +"\nAct : "+ actProduct);
    }

    public Product getAProductFromDbById() {
        Product product = new Product();
        // Database connection details
        String jdbcURL = "jdbc:mysql://localhost:3306/shop";
        String dbUser = "auto";
        String dbPassword = "auto123";

        // SQL query to retrieve all rows from the products table
        String query = "SELECT * FROM product WHERE id = UNHEX('"
                + productUuid.toString().replace("-","")
                +"')";

        // Connection, PreparedStatement, and ResultSet objects
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Establishing the connection
            connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
            System.out.println("Connected to the database!");

            // Preparing and executing the SQL query
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            //Get the record
            while (resultSet.next()) {
                UUID id = getUUIDFromBytes(resultSet.getBytes("id"));
                product.setId(id);
                String name = resultSet.getString("name"); // Assuming 'name' is a column
                product.setName(name);
                String description = resultSet.getString("description"); // Assuming 'price' is a column
                product.setDescription(description);
                float price = resultSet.getInt("price"); // Assuming 'quantity' is a column
                product.setPrice(price);
                // Printing the row data
                System.out.printf("Record - ID: %s, Name: %s, Description: %s, Quantity: %.2f", id.toString(), name, description, price);
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Closing resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return product;
    }
    private UUID getUUIDFromBytes(byte[] bytes) {
        if (bytes == null || bytes.length != 16) {
            throw new IllegalArgumentException("Invalid UUID byte array");
        }
        long mostSigBits = 0;
        long leastSigBits = 0;
        for (int i = 0; i < 8; i++) {
            mostSigBits = (mostSigBits << 8) | (bytes[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            leastSigBits = (leastSigBits << 8) | (bytes[i] & 0xff);
        }
        return new UUID(mostSigBits, leastSigBits);
    }
}
