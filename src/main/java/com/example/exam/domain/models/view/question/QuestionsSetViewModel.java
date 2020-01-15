package com.example.exam.domain.models.view.question;

import java.util.List;

public class QuestionsSetViewModel {

    private List<QuestionsAskedViewModel> answers;
    private String questionSetId;

    public List<QuestionsAskedViewModel> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionsAskedViewModel> answers) {
        this.answers = answers;
    }

    public String getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(String questionSetId) {
        this.questionSetId = questionSetId;
    }
}
