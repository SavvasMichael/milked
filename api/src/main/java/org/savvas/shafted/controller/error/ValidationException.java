package org.savvas.shafted.controller.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {

    private List<String> errors = new ArrayList<>();

    public ValidationException(List<FieldError> errors) {
        for (FieldError error : errors) {
            this.errors.add(error.getField());
        }
    }
    public ValidationException(String errorMessage) {
        this.errors.add(errorMessage);
    }

    public List<String> getErrors() {
        return errors;
    }
}
