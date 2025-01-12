package ca.qaguru.shoptestsuite.tests;

import ca.qaguru.shoptestsuite.lib.TestBase;
import ca.qaguru.shoptestsuite.models.Product;
import ca.qaguru.shoptestsuite.services.ProductService;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;


@Slf4j
public class ProductAPITests extends TestBase {

    @Test(description = "Save a product")
    public void validateSavingAProduct(){
        Product product = Product.builder()
                .name("LG 43\" Full HD LED TV - 43LF5100")
                .description("The LG LF5100 Edge Lit LED TV has a " +
                        "sleek and slim profile with the benefit " +
                        "of bright and clear colour detail and energy efficiency.")
                .price(600F)
                .build();


        log(Status.INFO,"Payload :" + product);
        ProductService
                .builder()
                .requestSpecification(requestSpecification)
                .build()
                .saveNewProduct(product, HttpStatus.SC_CREATED)
                .verifyProductInDb(product);

    }

    @Test
    public void validateFindingAProduct(){
        Product product = Product.builder()
                .name("LG 43\" Full HD LED TV - 43LF5100")
                .description("The LG LF5100 Edge Lit LED TV has a " +
                        "sleek and slim profile with the benefit " +
                        "of bright and clear colour detail and energy efficiency.")
                .price(600F)
                .build();

        ProductService productService = ProductService
                .builder()
                .requestSpecification(requestSpecification)
                .build();


        productService
                .saveNewProduct(product,HttpStatus.SC_CREATED)
                .findProductById(productService.getProductUuid(),HttpStatus.SC_OK,product);
    }
    @Test
    public void validateUpdatingAProduct(){
        Product product = Product.builder()
                .name("LG 43\" Full HD LED TV - 43LF5100")
                .description("The LG LF5100 Edge Lit LED TV has a " +
                        "sleek and slim profile with the benefit " +
                        "of bright and clear colour detail and energy efficiency.")
                .price(600F)
                .build();

        Product productToUpdate = Product.builder()
                .name("Samsung 57\" Full HD LED TV - 43LF5100")
                .description("The LG LF5100 Edge Lit LED TV has a " +
                        "sleek and slim profile with the benefit " +
                        "of bright and clear colour detail and energy efficiency.")
                .price(1600F)
                .build();

        ProductService productService = ProductService
                .builder()
                .requestSpecification(requestSpecification)
                .build();

        productService
                .saveNewProduct(product,HttpStatus.SC_CREATED)
                .updateProduct(productService.getProductUuid(),productToUpdate, HttpStatus.SC_NO_CONTENT)
                .findProductById(productService.getProductUuid(),HttpStatus.SC_OK,productToUpdate);
    }
    @Test
    public void validateDeleteProductById(){
        Product product = Product.builder()
                .name("LG 43\" Full HD LED TV - 43LF5100")
                .description("The LG LF5100 Edge Lit LED TV has a " +
                        "sleek and slim profile with the benefit " +
                        "of bright and clear colour detail and energy efficiency.")
                .price(600F)
                .build();
        ProductService productService = ProductService
                .builder()
                .requestSpecification(requestSpecification)
                .build();

        productService
                .saveNewProduct(product,HttpStatus.SC_CREATED)
                .deleteProduct(productService.getProductUuid(),HttpStatus.SC_NO_CONTENT)
                .findProductById(productService.getProductUuid(), HttpStatus.SC_INTERNAL_SERVER_ERROR,null);
    }
    @Test
    public void validateFindAllProducts(){
        ObjectMapper objectMapper = new ObjectMapper();
        Product[] products = null;
        try {
            // Load the file from the resources folder
            InputStream inputStream = ProductAPITests.class.getClassLoader().getResourceAsStream("products.json");

            if (inputStream == null) {
                throw new IllegalArgumentException("File not found in resources folder");
            }

            // Read the JSON file and convert to Product array
            products = objectMapper.readValue(inputStream, Product[].class);

            // Print each product
            for (Product product : products) {
                System.out.println(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ProductService productService = ProductService
                .builder()
                .requestSpecification(requestSpecification)
                .build();

        for (Product product : products) {
            productService
                    .saveNewProduct(product,HttpStatus.SC_CREATED)
                    .setProductIdFor(product);
        }
        productService
                .findAllProducts(HttpStatus.SC_OK, List.of(products));
    }

}
