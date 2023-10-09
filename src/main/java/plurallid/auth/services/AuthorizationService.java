package plurallid.auth.services;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;

import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import io.vertx.core.json.JsonObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeAll;
import plurallid.auth.config.*;
import plurallid.enums.App;

public interface AuthorizationService extends App {

    public default void validate(CurrentVertxRequest request) throws Exception {
        String applicationKeyFromHeader = getApplicationKeyFromHeader(request);
        if (applicationKeyFromHeader == null || Objects.equals(applicationKeyFromHeader, "")) {
            switch(getStatusCodeFromResponse(request, null)) {
                case 401:
                    throw new AuthenticationUnauthorizedError();
                case 406:
                    throw new NotAcceptableError();
                case 502:
                    throw new BadGatewayError();
                default:
            }
        }
        return;
    }

    public default void hasToken(CurrentVertxRequest request) {
        String tokenFromHeader = getTokenFromHeader(request);
        if (tokenFromHeader == null || Objects.equals(tokenFromHeader, "")) {
          throw new AuthenticationTokenRequiredError();
        }
    }

    public default int getStatusCodeFromResponse(CurrentVertxRequest request, String applicationIdFromJwtToken) throws Exception {
        String applicationKeyFromBody;
        if (applicationIdFromJwtToken != null) {
            applicationKeyFromBody = applicationIdFromJwtToken;
        } else {
            applicationKeyFromBody = getApplicationKeyFromBody(request);
        }
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        final RequestBody body = RequestBody.create(mediaType, "{\n\t\"idApplication\": \"" + applicationKeyFromBody + "\"\n}");
        final Request req = new Request.Builder()
            .url(CADUNI_URL + "Aplicacoes/busca.json")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("idApplication", CADUNI_ID_APPLICATION)
            .addHeader("Cache-Control", "no-cache, no-store, must-revalidate, private")
            .addHeader("Pragma", "no-cache")
            .addHeader("Expires", String.valueOf(0))
            .build();
        final Response response = client.newCall(req).execute();
        int code = response.code();
        response.body().close();
        return code;
    }

    default String getApplicationKeyFromHeader(CurrentVertxRequest request) {
        return request.getCurrent().request().getHeader("idApplication");
    }

    default String getTokenFromHeader(CurrentVertxRequest request) {
        return request.getCurrent().request().getHeader("Authorization");
    }

    default String getApplicationKeyFromBody(CurrentVertxRequest request) {
        JsonObject applicationId = request.getCurrent().body().asJsonObject().getJsonObject("variables");
        return applicationId.getString("applicationId");
    }

    public default void isAuthorized(CurrentVertxRequest request) throws Exception {
        String authorizationHeaderValue = request.getCurrent().request().getHeader("Authorization");
        String token = authorizationHeaderValue.substring(7);
        String decodedToken = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);
        JsonObject application = new JsonObject(decodedToken);
        String applicationIdFromJwtToken = application.getString("applicationId");
        isInvalidToken(request, applicationIdFromJwtToken);
        isExpiredToken(decodedToken);
    }

    public default void isInvalidToken(CurrentVertxRequest request, String applicationIdFromJwtToken) throws Exception {
        if (getStatusCodeFromResponse(request, applicationIdFromJwtToken) != 200) {
            throw new InvalidTokenError();
        }
    }

    public default void isExpiredToken(String decodedToken) {
        JsonObject json = new JsonObject(decodedToken);
        Instant instantOfTokenExpirationDate = Instant.ofEpochSecond(Long.parseLong(json.getString("exp")));
        if (instantOfTokenExpirationDate.compareTo(Instant.now()) < 0) {
            throw new ExpiredTokenError();
        }
    }

}
