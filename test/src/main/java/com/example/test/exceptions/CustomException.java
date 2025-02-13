package com.example.test.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends Exception{

    private HttpStatus status;

    /**
     * Constructor de la clase
     * @param status Código de error
     * @param message Mensaje de error
     */
    public CustomException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}
