package com.example.exam.service;

public interface QuestionService {

    String QUESTION_DELIMITER_REGEX = "Question [1-9]+.+";

    void saveTextFileDataToDB(String fileName);

}
