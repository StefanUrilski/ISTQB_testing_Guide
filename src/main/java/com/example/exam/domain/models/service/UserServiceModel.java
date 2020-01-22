package com.example.exam.domain.models.service;

import com.example.exam.domain.models.service.question.QuestionServiceModel;

import java.util.Set;

public class UserServiceModel {

    private String id;
    private String userName;
    private String password;
    private Set<UserRoleServiceModel> authorities;
    private Set<QuestionServiceModel> questions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRoleServiceModel> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<UserRoleServiceModel> authorities) {
        this.authorities = authorities;
    }

    public Set<QuestionServiceModel> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QuestionServiceModel> questions) {
        this.questions = questions;
    }
}
