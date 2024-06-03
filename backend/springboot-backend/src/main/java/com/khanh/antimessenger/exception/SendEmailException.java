package com.khanh.antimessenger.exception;

public class SendEmailException extends RuntimeException {

    public SendEmailException() {
        super(String.format("Emai was not successfully sent"));
    }

}
