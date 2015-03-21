package org.savvas.shafted.controller.error;

import java.util.List;

public class ErrorResponse {
    private List<String> errors;
    ErrorResponse(){}
    public ErrorResponse(List<String> errors){
        this.errors = errors;
    }
    public List<String> getErrors() {
        return errors;
    }
}

