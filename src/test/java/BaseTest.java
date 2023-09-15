import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    @BeforeSuite
    public void setup() {
        RestAssured.requestSpecification = new RequestSpecBuilder().build()
                .baseUri("https://gorest.co.in/public/v2")
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer 48919a6f3c0ca97da9dea497cff302fa9499f8b93019409f1eccc27a1788fc8d"));
    }
}

