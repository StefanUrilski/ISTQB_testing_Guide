package com.example.exam.domain.models.view.question;

import java.util.Set;

public class QuestionsSetViewModel {

    private String question;
    private Set<AnswerViewModel> answers;
    private String questionSetId;

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

    public String getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(String questionSetId) {
        this.questionSetId = questionSetId;
    }
}
