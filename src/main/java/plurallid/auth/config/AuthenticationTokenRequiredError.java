package plurallid.auth.config;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AuthenticationTokenRequiredError extends RuntimeException implements GraphQLError {

    private String AUTHENTICATION_TOKEN_REQUIRED = "authentication_token_required";

    public AuthenticationTokenRequiredError() {
        super("Authentication token required. Put it and try again.");
    }

    @Override
    public List<SourceLocation> getLocations() {
        return new ArrayList<>();
    }

    @Override
    public ErrorClassification getErrorType() {
        return ErrorType.DataFetchingException;
    }

    @Override
    public Map<String, Object> getExtensions() {
        Map<String, Object> extensions = new HashMap<>();
        extensions.put("code", AUTHENTICATION_TOKEN_REQUIRED);
        return extensions;
    }
    
}
