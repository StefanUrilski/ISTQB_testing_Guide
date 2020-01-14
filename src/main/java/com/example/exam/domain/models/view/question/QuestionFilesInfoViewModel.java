package com.example.exam.domain.models.view.question;

public class QuestionFilesInfoViewModel {

    private String fileName;
    private boolean isAdded;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }
}
