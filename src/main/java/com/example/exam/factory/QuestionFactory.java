package com.example.exam.factory;

import com.example.exam.domain.entities.Question;

public interface QuestionFactory {

    String QUESTION_ANSWER_DELIMITER = "Correct answer: ";

    Question buildQuestion(String conditionText);
}
