package com.example.exam.domain.models.service.question;

import java.util.Set;

public class QuestionAskedServiceModel {

    private long id;
    private String question;
    private Set<AnswerServiceModel> answers;

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

    public Set<AnswerServiceModel> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<AnswerServiceModel> answers) {
        this.answers = answers;
    }

}
