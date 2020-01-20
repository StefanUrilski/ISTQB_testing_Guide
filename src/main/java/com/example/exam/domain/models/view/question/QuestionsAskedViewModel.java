package com.example.exam.domain.models.view.question;

import java.util.List;
import java.util.Set;

public class QuestionsAskedViewModel {

    private long id;
    private String question;
    private List<AnswerViewModel> answers;

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

    public List<AnswerViewModel> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerViewModel> answers) {
        this.answers = answers;
    }
}
