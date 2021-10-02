package com.tayfurunal.productservice.exception;

import org.springframework.http.HttpStatus;

public class ProductServiceNotFoundException extends ProductServiceException {

    public ProductServiceNotFoundException(String key) {
        super(key, HttpStatus.NOT_FOUND);
    }
}
