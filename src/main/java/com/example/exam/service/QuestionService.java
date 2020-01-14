package com.example.exam.service;

import com.example.exam.domain.models.service.QuestionInfoServiceModel;

public interface QuestionService {

    String QUESTION_DELIMITER_REGEX = "Question [1-9]+.+";

    void saveTextFileDataToDB(String fileName);

    QuestionInfoServiceModel getQuestionsInfo(String username);
}
