import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.testng.Assert;
import org.hamcrest.Matchers;
import org.hamcrest.core.Every;
import org.testng.annotations.Test;
import request.UserData;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class TestGetUsers extends BaseTest {

    @Test

    public void getUsers() {
        List<UserData> allUsers = when()
                .get("users")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("id", Every.everyItem(
                        Matchers.isA(Integer.class)))
                .extract().jsonPath().getList("id");

        System.out.println(allUsers);
    }

    @Test
    public UserData createNewUser() {
        Faker faker = new Faker();
        UserData expectedUser = UserData.builder()
                .email(faker.internet().emailAddress())
                .name(faker.name().name())
                .gender("female")
                .status("active").build();

        UserData actualUser = given()
                .body(expectedUser)
                .when()
                .post("users")
                .then()
                .log()
                .all()
                .statusCode(201)
                .body("id", Matchers.isA(Integer.class))
                .extract().as(UserData.class);
        Assert.assertEquals(expectedUser, actualUser);
        return actualUser;
    }

    @Test
    public void updateUser() {
        UserData randomUser = createNewUser();
        randomUser.setName("NewName");
        UserData updatedUser = given()
                .body(randomUser)
                .pathParams("id", randomUser.getId())
                .when()
                .patch("users/{id}")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("id", Matchers.isA(Integer.class))
                .extract().as(UserData.class);
        Assert.assertEquals(randomUser, updatedUser);
        System.out.println(randomUser.getName());
    }

    @Test
    public void deleteUser() {
        RestAssured.defaultParser = Parser.JSON;
        UserData newUser = createNewUser();
        UserData userToBeDeleted = given()
                .body(newUser)
                .pathParams("id", newUser.getId())
                .when()
                .delete("users/{id}")
                .then()
                .log()
                .all()
                .statusCode(204)
                .extract().as(UserData.class);
        Assert.assertNull(userToBeDeleted);
    }
}
