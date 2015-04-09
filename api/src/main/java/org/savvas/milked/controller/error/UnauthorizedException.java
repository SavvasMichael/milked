package org.savvas.milked.controller.error;

public class UnauthorizedException extends RuntimeException {

    private String error;

    public UnauthorizedException(String error) {
        this.error=error;
    }
    public String getError(){
        return error;
    }
}

