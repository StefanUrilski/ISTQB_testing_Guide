package com.example.exam.domain.models.service.question;


import java.util.List;

public class QuestionsSetServiceModel {

    private List<QuestionServiceModel> questions;
    private String questionSetId;
    private List<FigureServiceModel> tables;

    public List<QuestionServiceModel> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionServiceModel> questions) {
        this.questions = questions;
    }

    public String getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(String questionSetId) {
        this.questionSetId = questionSetId;
    }

    public List<FigureServiceModel> getTables() {
        return tables;
    }

    public void setTables(List<FigureServiceModel> tables) {
        this.tables = tables;
    }
}
