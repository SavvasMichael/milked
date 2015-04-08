package org.savvas.milked.controller.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private List<String> errors = new ArrayList<>();

    ErrorResponse(){}

    public ErrorResponse(String error) {
        this.errors.add(error);
    }

    public ErrorResponse(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}

