package plurallid.auth.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;


public class AuthenticationUnauthorizedError extends RuntimeException implements GraphQLError {

    private String ATHENTICATION_UNAUTHORIZED = "authentication_unauthorized";

    public AuthenticationUnauthorizedError() {
        super("Authentication unauthorized");
    }

    @Override
    public List<SourceLocation> getLocations() {
        return new ArrayList<>();
    }
    
    @Override
    public ErrorClassification getErrorType() {
        return ErrorType.ValidationError;
    }

    @Override
    public Map<String, Object> getExtensions() {
        Map<String, Object> extensions = new HashMap<>();
        extensions.put("code", ATHENTICATION_UNAUTHORIZED);
        return extensions;
    }

}
