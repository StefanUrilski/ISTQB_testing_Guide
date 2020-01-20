package com.example.exam.domain.models.view;

import com.example.exam.domain.models.view.question.AnswerViewModel;

import java.util.Set;

public class TestAnswerViewModel {

    private long id;
    private String question;
    private Set<AnswerViewModel> answers;
    private char correctAnswer;
    private String explanation;
    private boolean valid;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Set<AnswerViewModel> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<AnswerViewModel> answers) {
        this.answers = answers;
    }

    public char getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(char correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
