package ca.qaguru.shoptestsuite.lib;

import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServiceBase {
    protected RequestSpecification requestSpecification;
}
