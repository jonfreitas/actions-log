package plurallid.auth.exceptions;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequiredFieldError extends RuntimeException implements GraphQLError {

    private String REQUIRED_FIELD = "required_field";

    public RequiredFieldError(String requiredField) {
        super("Validation error (WrongType@[createAction]) : in argument 'action' is missing required fields: [" + requiredField + "]");
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
        extensions.put("code", REQUIRED_FIELD);
        return extensions;
    }

}
