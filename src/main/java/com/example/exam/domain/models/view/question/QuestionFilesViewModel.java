package com.example.exam.domain.models.view.question;

import java.util.List;

public class QuestionFilesViewModel {

    private List<QuestionFilesInfoViewModel> questionFiles;

    public List<QuestionFilesInfoViewModel> getQuestionFiles() {
        return questionFiles;
    }

    public void setQuestionFiles(List<QuestionFilesInfoViewModel> questionFiles) {
        this.questionFiles = questionFiles;
    }
}
