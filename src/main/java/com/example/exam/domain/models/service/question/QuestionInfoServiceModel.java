package com.example.exam.domain.models.service.question;

public class QuestionInfoServiceModel {

    private Integer[] setOfQuestions;
    private int randomCoverage;

    public QuestionInfoServiceModel() {
    }

    public QuestionInfoServiceModel(int setOfQuestions, int randomCoverage) {
        this.setOfQuestions = new Integer[setOfQuestions];
        this.randomCoverage = randomCoverage;
    }

    public Integer[] getSetOfQuestions() {
        return setOfQuestions;
    }

    public void setSetOfQuestions(Integer[] setOfQuestions) {
        this.setOfQuestions = setOfQuestions;
    }

    public int getRandomCoverage() {
        return randomCoverage;
    }

    public void setRandomCoverage(int randomCoverage) {
        this.randomCoverage = randomCoverage;
    }
}
