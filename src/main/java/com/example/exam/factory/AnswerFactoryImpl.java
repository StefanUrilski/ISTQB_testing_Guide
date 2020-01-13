package com.example.exam.factory;

import com.example.exam.domain.entities.Answer;
import com.example.exam.errors.WrongQuestionAnswerTextException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.exam.common.Constants.WRONG_QUESTION_ANSWER_TEXT;

@Component
public class AnswerFactoryImpl implements AnswerFactory {

    @Override
    public Set<Answer> buildAnswer(String[] setOfAnswers) {
      return Arrays.stream(setOfAnswers)
                .map(fullAnswer -> {
                    String[] answerSet = fullAnswer.split("\\) ");

                    if (answerSet.length != 2) {
                        throw new WrongQuestionAnswerTextException(WRONG_QUESTION_ANSWER_TEXT);
                    }
                    char symbol = answerSet[0].charAt(0);

                    return new Answer(symbol, answerSet[1]);
                })
                .collect(Collectors.toSet());
    }
}
