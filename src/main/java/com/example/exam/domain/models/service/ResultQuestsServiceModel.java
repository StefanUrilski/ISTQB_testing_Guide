package com.example.exam.domain.models.service;

import com.example.exam.domain.models.service.question.FigureServiceModel;

import java.util.List;

public class ResultQuestsServiceModel {

    private List<TestAnswerServiceModel> correctAnswers;
    private List<FigureServiceModel> tables;

    public List<TestAnswerServiceModel> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<TestAnswerServiceModel> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public List<FigureServiceModel> getTables() {
        return tables;
    }

    public void setTables(List<FigureServiceModel> tables) {
        this.tables = tables;
    }
}
