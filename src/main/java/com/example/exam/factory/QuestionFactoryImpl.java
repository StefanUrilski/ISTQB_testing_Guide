package com.example.exam.factory;

import com.example.exam.domain.entities.Answer;
import com.example.exam.domain.entities.Question;
import com.example.exam.errors.WrongQuestionAnswerTextException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

import static com.example.exam.common.Constants.WRONG_QUESTION_ANSWER_TEXT;

@Component
public class QuestionFactoryImpl implements QuestionFactory {

    private final AnswerFactory answerFactory;

    @Autowired
    public QuestionFactoryImpl(AnswerFactory answerFactory) {
        this.answerFactory = answerFactory;
    }

    @Override
    public Question buildQuestion(String conditionText) {
        String[] text = conditionText.split(QUESTION_ANSWER_DELIMITER);

        String[] qAndA = text[0].split("\\(");

        if (text.length != 2 && qAndA.length < 2) {
            throw new WrongQuestionAnswerTextException(WRONG_QUESTION_ANSWER_TEXT);
        }

        char correctAnswer = text[1].charAt(0);
        String condition = qAndA[0];
        String explanation = text[1].substring(1).trim();
        Set<Answer> answers = answerFactory.buildAnswer(Arrays.stream(qAndA).skip(1).toArray(String[]::new));

        Question question = new Question();

        question.setQuestion(condition);
        question.setCorrectAnswer(correctAnswer);
        question.setExplanation(explanation);
        question.setAnswers(answers);

        return question;
    }
}
