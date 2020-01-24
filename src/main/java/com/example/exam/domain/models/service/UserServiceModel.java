package com.example.exam.domain.models.service;

import java.util.Set;

public class UserServiceModel {

    private String id;
    private String userName;
    private String password;
    private Set<UserRoleServiceModel> authorities;
    private Set<Long> visitedQuestions;

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

    public Set<Long> getVisitedQuestions() {
        return visitedQuestions;
    }

    public void setVisitedQuestions(Set<Long> visitedQuestions) {
        this.visitedQuestions = visitedQuestions;
    }
}
