package plurallid;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ActionResourceTest {

	private static final String BEARER_TOKEN = "eyJraWQiOiIvcHJpdmF0ZUtleS5wZW0iLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJwbHVyYWxsaWQiLCJqdGkiOiJhLTEyMyIsInN1YiI6Impkb2UtdXNpbmctand0LXJiYWMiLCJ1cG4iOiJqZG9lQHF1YXJrdXMuaW8iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqZG9lIiwiYXVkIjoidXNpbmctand0LXJiYWMiLCJ0eXAiOiJKV1QiLCJhdXRoX3RpbWUiOm51bGwsImlhdCI6MTY5NjkzNzk1NCwiYXBwbGljYXRpb25JZCI6IkVFNUEzMzYwN0E1RDkyREM4QjdCQTFBNDg2MTQ1RkNFIiwiZXhwIjoxNjk3MDI0MzU0fQ.H-N4hRjTjoE17FilMhZUJ90IGJOeUunlpEd8wgsMdv44gzTTAtNC2nuNwpHwrtUuAyt8BgoQMXqGTmP0w2S5qKH8kNnZU7i2DzQzdRKsVqFqA0DF7V_DXY4cWmCRXr0XlVLTpizeJuE6EFu9AN5FttHdOJ-kuPeU03qF6GXc2w3EVFl7MZgb58Bct50ePUMurY16beDKQMKO5oU3sfZRDSi2BlOJse5g_SlLw1DxZSSIcFlC05JBKhbZl3IjZWxGlFzxNjHgg3lBGUsp3nZxcSfXnoKblC5kNx_8A1SNES04fCKYUlsHn_Yqfa-U35FR1RBHzN6oUmlRqw_wzj97oA";

	@Test
	public void getActions() {

		String requestBody =
				"query {" +
						"actions(entityValue: 504460, entityId: 24) {" +
							" entityId" +
							" entityName" +
							" entityValue" +
							" changes { " +
								"who { " +
									" personId" +
									" admId" +
								"}" +
								" message" +
								" when" +
								" applicationId" +
							"}" +
						"}" +
				"}";

		given()
				.header("Authorization", "Bearer " + BEARER_TOKEN)
				.header("Content-Type", "text/plain; charset=utf-8")
				.body(requestBody)
				.when()
				.post("/graphql/")
				.then()
				.statusCode(200);
	}

	@Test
	public void addAction() {

		String requestBody =
				"mutation addAction { " +
					"createAction" +
						"(action: " +
							"{"+
								"entityId: 24," +
								"entityName: \"Adoção\", " +
								"entityValue: 504461," +
								"changes: " +
									"["+
										"{"+
											"who: " +
												"{"+
													"personId: null," +
													"admId: 22" +
												"}"+
											"message: \\\"Disciplina(s) Matemática, Português adicionada(s) à coleção Sistema Anglo - Básico no 7º ANO\\\", " +
											"when: \\\"2023-10-04T09:27:23\\\", " +
											"applicationId: \\\"TESTEAPPID\\\" " +
										"}" +
									"]"+
							"}" +
						")" +
					"{" +
						"entityValue " +
					"}" +
				"}";

		given()
				.header("Authorization", "Bearer " + BEARER_TOKEN)
				.header("Content-Type", "text/plain; charset=utf-8")
				.body(requestBody)
				.when()
				.post("/graphql/")
				.then()
				.statusCode(200);

	}

}