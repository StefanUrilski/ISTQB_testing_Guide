package com.example.exam.domain.models.service.question;

import java.util.Set;

public class QuestionAskedServiceModel {

    private String question;
    private Set<AnswerServiceModel> answers;

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
