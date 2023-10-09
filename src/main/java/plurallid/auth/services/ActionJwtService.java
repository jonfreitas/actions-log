package plurallid.auth.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.context.annotation.Configuration;

import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import io.vertx.core.json.JsonObject;
import plurallid.auth.config.NotAcceptableError;
import plurallid.auth.helper.JwtActionUtil;

@Configuration
@ApplicationScoped
public class ActionJwtService implements AuthorizationService {

    @Inject
    CurrentVertxRequest request;

    private String applicationId;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String generateJwt(String applicationId) throws Exception {
        String generatedToken = JwtActionUtil.generateTokenString("/JWTClaims.json", null, applicationId);
        String decodedToken = new String(Base64.getUrlDecoder().decode(generatedToken.split("\\.")[1]), StandardCharsets.UTF_8);
        JsonObject application = new JsonObject(decodedToken);
        String applicationIdFromJwtToken = application.getString("applicationId");
        boolean isValidApplicationId = getStatusCodeFromResponse(request, applicationIdFromJwtToken) == 200;
        if (!isValidApplicationId) {
            throw new NotAcceptableError();
        }
        return generatedToken;
    }

}
