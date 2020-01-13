package com.example.exam.factory;

import com.example.exam.domain.entities.Answer;

import java.util.Set;

public interface AnswerFactory {

    Set<Answer> buildAnswer(String[] setOfAnswers);
}
