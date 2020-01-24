package com.example.exam.domain.models.service.question;

import com.example.exam.domain.models.service.UserServiceModel;

import java.util.List;
import java.util.Set;

public class QuestionServiceModel {

    private long id;
    private String question;
    private List<AnswerServiceModel> answers;
    private char correctAnswer;
    private String explanation;
    private String questionSet;
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

    public List<AnswerServiceModel> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerServiceModel> answers) {
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

    public String getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(String questionSet) {
        this.questionSet = questionSet;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
