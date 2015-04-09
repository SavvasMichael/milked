package org.savvas.milked.controller.error;

public class PasswordDoesNotMatchException extends RuntimeException {

    private String error;

    public PasswordDoesNotMatchException(String error){
        this.error=error;
    }
    public String getError(){
        return error;
    }
}

