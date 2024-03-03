package com.ecommerce.shopapp.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ValidationException {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            try {
                var beanClazz = Objects.requireNonNull(ex.getTarget()).getClass();
                var field = beanClazz.getDeclaredField(fieldError.getField());
                var jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);

                if (jsonPropertyAnnotation != null) {
                    errors.put(jsonPropertyAnnotation.value(), fieldError.getDefaultMessage());
                } else {
                    LoggerFactory.getLogger(getClass()).debug("Failed to get annotated field at {}", fieldError.getField());
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
            } catch (NoSuchFieldException e) {
                LoggerFactory.getLogger(getClass()).error("Failed to get annotated field", e);
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }

        return ResponseEntity.badRequest().body(errors).getBody();
    }
}
