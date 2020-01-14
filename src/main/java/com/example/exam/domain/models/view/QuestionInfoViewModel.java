package com.example.exam.domain.models.view;

public class QuestionInfoViewModel {

    private Integer[] setOfQuestions;
    private int random;

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
