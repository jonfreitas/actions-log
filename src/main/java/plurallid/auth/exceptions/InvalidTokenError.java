package plurallid.auth.exceptions;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class InvalidTokenError extends RuntimeException implements GraphQLError {

    private String INVALID_TOKEN = "invalid_token";

    public InvalidTokenError() {
        super("Application token is not valid. Check it and try again.");
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
        extensions.put("code", INVALID_TOKEN);
        return extensions;
    }
    
}
