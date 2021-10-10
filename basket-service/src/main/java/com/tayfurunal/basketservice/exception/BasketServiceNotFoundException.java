package com.tayfurunal.basketservice.exception;

import org.springframework.http.HttpStatus;

public class BasketServiceNotFoundException extends BasketServiceException {

    public BasketServiceNotFoundException(String key) {
        super(key, HttpStatus.NOT_FOUND);
    }
}
