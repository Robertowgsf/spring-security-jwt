package com.robertowgsf.springsecurityjwt.exception.handler;

import com.robertowgsf.springsecurityjwt.exception.model.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestControllerAdvice
public class ValidationControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ValidationError> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return getValidationErrors(ex);
    }

    private List<ValidationError> getValidationErrors(MethodArgumentNotValidException ex) {
        var validationErrors = new ArrayList<ValidationError>();
        var results = new HashMap<String, List<String>>();

        for (var error : ex.getBindingResult().getFieldErrors()) {
            if (!results.containsKey(error.getField())) {
                results.put(error.getField(), new ArrayList<>());
            }

            results.get(error.getField()).add(error.getDefaultMessage());
        }

        for (var result : results.keySet()) {
            validationErrors.add(new ValidationError(result, results.get(result)));
        }

        return validationErrors;
    }
}

