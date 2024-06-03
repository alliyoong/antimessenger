package com.khanh.antimessenger.utilities;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static com.khanh.antimessenger.constant.ExceptionConstant.INVALID_UPLOAD_FILE;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FileUploadValidator.class})
public @interface ValidUploadFile {
    String message() default INVALID_UPLOAD_FILE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
