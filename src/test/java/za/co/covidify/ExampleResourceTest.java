package za.co.covidify;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

//@QuarkusTest
public class ExampleResourceTest {

  // @Test
  public void testHelloEndpoint() {
    given().when().get("/hello").then().statusCode(200).body(is("hello"));
  }

}