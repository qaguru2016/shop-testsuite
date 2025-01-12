package ca.qaguru.shoptestsuite.lib;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.service.ExtentTestManager;
import com.aventstack.extentreports.testng.listener.ExtentITestListenerClassAdapter;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Listeners(ExtentITestListenerClassAdapter.class)
public class TestBase {
    protected String env;
    protected String baseUri;// Refer application.properties files
    protected final String basePath = "/api/v1";
    protected RequestSpecification requestSpecification;
    @BeforeSuite
    public void beforeSuite(){
        loadPropertyFile();
        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setBaseUri(baseUri)
                .setBasePath(basePath)
                .setContentType(ContentType.JSON)
                .build();

    }

    private void loadPropertyFile() {
        String propertyFile = System.getProperty("application.properties");
        System.out.println("Property file - " + propertyFile);
        Properties properties = new Properties();
        try (InputStream input = TestBase.class.getClassLoader().getResourceAsStream(propertyFile)) {
            if (input == null) {
                System.out.println("[Error] : Unable to find " + propertyFile);
                return;
            }

            // Load the properties file
            properties.load(input);

            // Access property values by key
            env = properties.getProperty("env");
            baseUri = properties.getProperty("base-uri");
            // Print the property values
            System.out.println("Environment: " + env);
            System.out.println("Base URI: " + baseUri);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void log(Status status, String msg){
        ExtentTestManager.getTest().log(status,msg);
    }


}
