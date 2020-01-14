package com.example.exam.domain.models.service.question;

import java.util.List;

public class QuestionFilesServiceModel {

    private List<QuestionFilesInfoServiceModel> questionFiles;

    public QuestionFilesServiceModel(List<QuestionFilesInfoServiceModel> questionFiles) {
        this.questionFiles = questionFiles;
    }

    public List<QuestionFilesInfoServiceModel> getQuestionFiles() {
        return questionFiles;
    }

    public void setQuestionFiles(List<QuestionFilesInfoServiceModel> questionFiles) {
        this.questionFiles = questionFiles;
    }
}
