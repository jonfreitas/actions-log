package plurallid.auth;

import org.eclipse.microprofile.graphql.*;

import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import plurallid.auth.services.ActionJwtService;
import plurallid.auth.services.AuthorizationService;

@GraphQLApi
@ApplicationScoped
public class ActionJwtResource implements AuthorizationService {

    @Inject
    ActionJwtService service;

    @Inject
    CurrentVertxRequest request;

    public ActionJwtResource(CurrentVertxRequest request) throws Exception {
        validate(request);
    }
    
    @Query("token")
    @Description("Get token")
    @PermitAll
    public String getJwt(@NonNull String applicationId) throws Exception {
        return service.generateJwt(applicationId);
    }

}
