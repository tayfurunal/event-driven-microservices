package com.tayfurunal.basketservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BasketServiceException extends RuntimeException {

    private final String key;
    private final String[] args;
    private final HttpStatus httpStatus;

    public BasketServiceException(String key, HttpStatus httpStatus, String... args) {
        this.key = key;
        this.httpStatus = httpStatus;
        this.args = args;
    }

    public BasketServiceException(String key, HttpStatus httpStatus) {
        this.key = key;
        this.httpStatus = httpStatus;
        this.args = new String[0];
    }
}