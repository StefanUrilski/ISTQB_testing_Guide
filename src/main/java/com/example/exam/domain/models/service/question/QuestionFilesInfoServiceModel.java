package com.example.exam.domain.models.service.question;

public class QuestionFilesInfoServiceModel {
    
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
