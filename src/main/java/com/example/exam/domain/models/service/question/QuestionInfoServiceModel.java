package com.example.exam.domain.models.service.question;

public class QuestionInfoServiceModel {

    private Integer[] setOfQuestions;
    private int random;

    public QuestionInfoServiceModel() {
    }

    public QuestionInfoServiceModel(int setOfQuestions, int random) {
        this.setOfQuestions = new Integer[setOfQuestions];
        this.random = random;
    }

    public Integer[] getSetOfQuestions() {
        return setOfQuestions;
    }

    public void setSetOfQuestions(Integer[] setOfQuestions) {
        this.setOfQuestions = setOfQuestions;
    }

    public int getRandom() {
        return random;
    }

    public void setRandom(int random) {
        this.random = random;
    }
}
