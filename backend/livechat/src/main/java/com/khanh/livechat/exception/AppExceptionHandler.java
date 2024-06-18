package com.khanh.livechat.exception;

import com.khanh.livechat.model.dto.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.khanh.livechat.constant.ExceptionConstant.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class AppExceptionHandler implements ErrorController{

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .httpStatusCode(status.value())
                        .httpStatus(status)
                        .reason(status.getReasonPhrase().toUpperCase())
                        .message(message.toUpperCase())
                        .data(null)
                        .build(),
                status
        );
    }

    @ExceptionHandler(ResourceAlreadyInUseException.class)
    public ResponseEntity<HttpResponse> resourceAlreadyInUseException(ResourceAlreadyInUseException e) {
        return createHttpResponse(BAD_REQUEST, e.getMessage().toUpperCase());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException() {
        return createHttpResponse(FORBIDDEN, NO_PERMISSION);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponse> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> errorMessages = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return createHttpResponse(BAD_REQUEST, errorMessages.toString()
                .replaceAll("\\[", "").replaceAll("\\]", ""));
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<HttpResponse> resourceNotFoundException(ResourceNotFound e) {
        return createHttpResponse(NOT_FOUND, e.getMessage().toUpperCase());
    }

//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<HttpResponse> noHandlerFoundException() {
//        return createHttpResponse(NOT_FOUND, "This way we disable spring default resource handler");
//    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> httpRequestNotSupportedException(HttpRequestMethodNotSupportedException e) {
        HttpMethod supportedMethod = Objects.requireNonNull(e.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED,supportedMethod));
    }

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<HttpResponse> notFoundUrl() {
        return createHttpResponse(NOT_FOUND, ERROR_URL_MSG);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> ioException(IOException e) {
        log.error(e.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerException(Exception e) {
        log.error(e.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }
}
