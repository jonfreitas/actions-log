package plurallid.auth.helper;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;


public class JwtErrorException extends RuntimeException implements GraphQLError {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }

    public JwtErrorException(String message, boolean writeStacktrace) {
        super(message, null, false, writeStacktrace);
        this.message = message;
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
        extensions.put("code", "Error");
        return extensions;
    }

}