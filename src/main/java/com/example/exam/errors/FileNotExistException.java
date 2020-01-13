package com.example.exam.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error occurred during reading file.")
public class FileNotExistException extends RuntimeException {

    public FileNotExistException(String message) {
        super(message);
    }
}