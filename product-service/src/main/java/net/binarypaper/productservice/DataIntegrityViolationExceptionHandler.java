package net.binarypaper.productservice;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DataIntegrityViolationExceptionHandler {

    private final HashMap<String, String> constraintsToValidate = new HashMap<>();

    private final Properties errorMessageProperties;

    public DataIntegrityViolationExceptionHandler(Properties errorMessageProperties) {
        this.errorMessageProperties = errorMessageProperties;
    }

    public void addConstraintValidation(String constraintName, String errorMessage) {
        if (constraintsToValidate.containsKey(constraintName)) {
            throw new RuntimeException("The constraint " + constraintName
                    + " has already been added");
        }
        constraintsToValidate.put(constraintName, errorMessage);
    }

    public ResponseStatusException handleException(DataIntegrityViolationException ex) {
        // Handle database constraint violations by throwing a
        // ResponseStatusException with the correct error code
        Set<String> constraintNames = constraintsToValidate.keySet();
        for (Throwable t = ex.getCause(); t != null; t = t.getCause()) {
            for (String constraintName : constraintNames) {
                if (t.getMessage().contains(constraintName)) {
                    String errorMessage = constraintsToValidate.get(constraintName);
                    if (errorMessage.startsWith("{") && errorMessage.endsWith("}")
                            && !errorMessageProperties.isEmpty()) {
                        errorMessage = errorMessageProperties
                                .getProperty(errorMessage.substring(1, errorMessage.length() - 1));
                    }
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
                }
            }
        }
        // If the DataIntegrityViolationException could not be handled
        throw ex;
    }

    public Properties getErrorMessageProperties() {
        return errorMessageProperties;
    }

}
