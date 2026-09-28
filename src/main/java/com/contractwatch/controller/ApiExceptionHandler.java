package com.contractwatch.controller;

import com.contractwatch.service.ContractNotFoundException;
import com.contractwatch.service.VendorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler
{
    @ExceptionHandler(ContractNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(
            ContractNotFoundException ex)
    {
        return Map.of(
                "error",
                ex.getMessage()
        );
    }

    @ExceptionHandler(VendorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> vendorNotFound(
            VendorNotFoundException ex)
    {
        return Map.of(
                "error",
                ex.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badRequest(
            IllegalArgumentException ex)
    {
        return Map.of(
                "error",
                ex.getMessage()
        );
    }
}
