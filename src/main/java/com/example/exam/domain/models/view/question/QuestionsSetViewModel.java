package com.example.exam.domain.models.view.question;

import java.util.List;

public class QuestionsSetViewModel {

    private List<QuestionsAskedViewModel> questionsSet;
    private String questionSetId;

    public List<QuestionsAskedViewModel> getQuestionsSet() {
        return questionsSet;
    }

    public void setQuestionsSet(List<QuestionsAskedViewModel> questionsSet) {
        this.questionsSet = questionsSet;
    }

    public String getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(String questionSetId) {
        this.questionSetId = questionSetId;
    }
}
