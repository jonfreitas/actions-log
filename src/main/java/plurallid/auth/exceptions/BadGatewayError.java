package plurallid.auth.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;


public class BadGatewayError extends RuntimeException implements GraphQLError {

    private String BAD_GATEWAY = "bad_gateway";

    public BadGatewayError() {
        super("Bad Gateway");
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
        extensions.put("code", BAD_GATEWAY);
        return extensions;
    }

}
