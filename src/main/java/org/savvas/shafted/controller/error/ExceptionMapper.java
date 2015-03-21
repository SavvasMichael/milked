package org.savvas.shafted.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
@ControllerAdvice
public class ExceptionMapper {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    ErrorResponse handleBadRequest(HttpServletRequest req, ValidationException ex) {
        return new ErrorResponse(ex.getErrors());
    }
}
