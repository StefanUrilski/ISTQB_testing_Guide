package com.example.exam.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error occurred during adding questions.")
public class QuestionSetFailureException extends RuntimeException {

    public QuestionSetFailureException(String message) {
        super(message);
    }
}
