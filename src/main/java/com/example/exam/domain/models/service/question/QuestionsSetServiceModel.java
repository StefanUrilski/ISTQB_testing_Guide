package com.example.exam.domain.models.service.question;


import java.util.List;

public class QuestionsSetServiceModel {

    private List<QuestionAskedServiceModel> questions;
    private String questionSetId;
    private List<FigureServiceModel> tables;

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

    public List<FigureServiceModel> getTables() {
        return tables;
    }

    public void setTables(List<FigureServiceModel> tables) {
        this.tables = tables;
    }
}
