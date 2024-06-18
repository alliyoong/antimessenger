package com.khanh.livechat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException{
    private Object fieldValue;
    private String fieldName;
    private String resourceName;

    public ResourceNotFound(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s cannot be found with %s: %s", resourceName, fieldName, fieldValue.toString()));
        this.fieldName = fieldName;
        this.resourceName = resourceName;
        this.fieldValue = fieldValue;
    }
}
