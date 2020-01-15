package com.example.exam.domain.models.view.question;

import java.util.Set;

public class QuestionsAskedViewModel {

    private String question;
    private Set<AnswerViewModel> answers;

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
}
