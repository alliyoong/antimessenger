package com.khanh.livechat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceAlreadyInUseException extends RuntimeException{
    private Object fieldValue;
    private String fieldName;
    private String resourceName;

    public ResourceAlreadyInUseException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("A(n) %s is already in use with %s: %s", resourceName, fieldName, fieldValue.toString()));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
