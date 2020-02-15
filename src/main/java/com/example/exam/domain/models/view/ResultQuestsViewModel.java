package com.example.exam.domain.models.view;

import com.example.exam.domain.models.view.question.FigureViewModel;

import java.util.List;

public class ResultQuestsViewModel {

    private List<TestAnswerViewModel> correctAnswers;
    private List<FigureViewModel> tables;
    private String scorePoints;

    public List<TestAnswerViewModel> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<TestAnswerViewModel> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public List<FigureViewModel> getTables() {
        return tables;
    }

    public void setTables(List<FigureViewModel> tables) {
        this.tables = tables;
    }

    public String getScorePoints() {
        return scorePoints;
    }

    public void setScorePoints(String scorePoints) {
        this.scorePoints = scorePoints;
    }
}
