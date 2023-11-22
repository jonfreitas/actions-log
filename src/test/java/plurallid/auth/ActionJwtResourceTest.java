package plurallid.auth;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class ActionJwtResourceTest {

    @Test
    public void getToken() {

        String requestBody =
                "query {" +
                        "token(applicationId: EE5A33607A5D92DC8B7BA1A486145FC) {" +
                        "}" +
                    "}" +
                "}";

        given()
                .header("Content-Type", "application/json; charset=utf-8")
                .body(requestBody)
                .when()
                .post("/graphql/")
                .then()
                .statusCode(200);
    }
}
