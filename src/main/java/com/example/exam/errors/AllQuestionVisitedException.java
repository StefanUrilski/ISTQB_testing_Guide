package com.example.exam.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Already existed file.")
public class AllQuestionVisitedException extends RuntimeException {

    public AllQuestionVisitedException() {
        super();
    }

    public AllQuestionVisitedException(String message) {
        super(message);
    }
}