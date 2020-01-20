package com.example.exam.domain.models.binding;

public class TestAnswerBindingModel {
    private char answerSymbol;
    private Long questionId;

    public TestAnswerBindingModel(char answerSymbol, Long questionId) {
        this.answerSymbol = answerSymbol;
        this.questionId = questionId;
    }

    public char getAnswerSymbol() {
        return answerSymbol;
    }

    public void setAnswerSymbol(char answerSymbol) {
        this.answerSymbol = answerSymbol;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}
