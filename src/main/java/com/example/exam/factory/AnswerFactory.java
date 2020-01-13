package com.example.exam.factory;

import com.example.exam.domain.entities.Answer;

import java.util.List;

public interface AnswerFactory {

    List<Answer> buildAnswer(String[] setOfAnswers);
}
