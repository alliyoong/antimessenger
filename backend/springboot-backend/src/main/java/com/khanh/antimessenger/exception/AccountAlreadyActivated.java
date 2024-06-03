package com.khanh.antimessenger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountAlreadyActivated extends RuntimeException{

        public AccountAlreadyActivated() {
            super(String.format("This account is already activated"));
        }
    }
