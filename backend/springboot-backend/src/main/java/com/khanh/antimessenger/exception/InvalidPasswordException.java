package com.khanh.antimessenger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPasswordException extends RuntimeException{

    public InvalidPasswordException() {
        super(String.format("Your password must contain numeric alpha " +
                "with at least 1 uppercase character, 1 special character with length between 8-30"));
    }
}
