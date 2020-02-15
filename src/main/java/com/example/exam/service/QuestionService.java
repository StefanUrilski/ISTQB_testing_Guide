package com.example.exam.service;

import com.example.exam.domain.models.binding.TestBindingModel;
import com.example.exam.domain.models.service.ResultQuestsServiceModel;
import com.example.exam.domain.models.service.question.QuestionFilesServiceModel;
import com.example.exam.domain.models.service.question.QuestionInfoServiceModel;
import com.example.exam.domain.models.service.question.QuestionsSetServiceModel;

public interface QuestionService {

    String QUESTION_DELIMITER_REGEX = "Question [1-9]+.+";

    void saveTextFileDataToDB(String fileName);

    QuestionInfoServiceModel getQuestionsInfo(String username);

    QuestionFilesServiceModel getFilesNames();

    QuestionsSetServiceModel getQuestionsByQuestionSetId(String questionSetId);

    ResultQuestsServiceModel checkAnswers(TestBindingModel testAnswers);

    QuestionsSetServiceModel getTenRandomQuestionByUser(String username);

    QuestionsSetServiceModel getFortyRandomQuestionByUser(String username);

    void startOver(String username);
}
