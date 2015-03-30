package org.savvas.shafted.controller.error;

public class NotFoundException extends RuntimeException {
    private String error;

    public NotFoundException(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
