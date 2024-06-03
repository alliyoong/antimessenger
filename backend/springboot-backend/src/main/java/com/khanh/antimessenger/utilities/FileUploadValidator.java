package com.khanh.antimessenger.utilities;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

import java.io.IOException;

import static com.khanh.antimessenger.constant.ExceptionConstant.INVALID_UPLOAD_FILE;

public class FileUploadValidator implements ConstraintValidator<ValidUploadFile, MultipartFile> {
    @Value("${application.upload.profile-image.max-size}")
    private long maxSize;

    @Override
    public void initialize(ValidUploadFile constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        boolean result = true;
        if (file == null) {
            return result;
        }
        String contentType = file.getContentType();
        Long fileSize = file.getSize();
        try {
            if (ImageIO.read(file.getInputStream()) == null || !isValidSize(fileSize) || !isSupportedContentType(contentType)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(INVALID_UPLOAD_FILE).addConstraintViolation();
                result = false;
            }
        } catch (IOException e) {
            result = false;
        }
        return result;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png") || contentType.equals("image/jpg") || contentType.equals("image/jpeg");
    }

    private boolean isValidSize(long size) {
        return size <= maxSize;
    }
}
