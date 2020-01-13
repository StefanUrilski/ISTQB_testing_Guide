package com.example.exam.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY, reason = "Wrong string input.")
public class WrongQuestionAnswerTextException extends RuntimeException {

    public WrongQuestionAnswerTextException(String message) {
        super(message);
    }
}
