package plurallid.auth.exceptions;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotAcceptableError extends RuntimeException implements GraphQLError {

    private String NOT_ACCEPTABLE = "not_acceptable";

    public NotAcceptableError() {
        super("Not Acceptable");
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
        extensions.put("code", NOT_ACCEPTABLE);
        return extensions;
    }

}
