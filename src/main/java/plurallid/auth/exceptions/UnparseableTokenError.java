package plurallid.auth.exceptions;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UnparseableTokenError extends RuntimeException implements GraphQLError {

    private String UNPARSEABLE_TOKEN = "unparseable_token";

    public UnparseableTokenError() {
        super("Unparseable token");
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
        extensions.put("code", UNPARSEABLE_TOKEN);
        return extensions;
    }
    
}
