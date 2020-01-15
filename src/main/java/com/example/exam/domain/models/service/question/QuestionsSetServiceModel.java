package com.example.exam.domain.models.service.question;


import java.util.List;

public class QuestionsSetServiceModel {

    private List<QuestionAskedServiceModel> questions;
    private String questionSetId;

    public List<QuestionAskedServiceModel> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionAskedServiceModel> questions) {
        this.questions = questions;
    }

    public String getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(String questionSetId) {
        this.questionSetId = questionSetId;
    }
}
