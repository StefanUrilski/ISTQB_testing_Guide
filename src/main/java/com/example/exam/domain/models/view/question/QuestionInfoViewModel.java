package com.example.exam.domain.models.view.question;

public class QuestionInfoViewModel {

    private Integer[] setOfQuestions;
    private int randomCoverage;

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
