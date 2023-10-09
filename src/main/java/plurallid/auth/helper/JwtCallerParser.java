package plurallid.auth.helper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import io.smallrye.jwt.auth.principal.*;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import plurallid.auth.config.InvalidTokenError;
import plurallid.auth.services.AuthorizationService;

@ApplicationScoped
@Alternative
@Priority(1)
public class JwtCallerParser extends JWTCallerPrincipalFactory implements AuthorizationService {

    @Inject
    CurrentVertxRequest request;

    @Override
    public JWTCallerPrincipal parse(String token, JWTAuthContextInfo authContextInfo) {
        try {
            String json = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);
            return new DefaultJWTCallerPrincipal(JwtClaims.parse(json));
        } catch (InvalidJwtException ex) {
            String message = new InvalidTokenError().getMessage();
            throw new JwtErrorException(message, false);
        }
    }

}
