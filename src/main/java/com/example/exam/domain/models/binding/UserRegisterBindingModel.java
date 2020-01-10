package com.example.exam.domain.models.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserRegisterBindingModel {

    private String userName;
    private String password;
    private String confirmPassword;

    @NotNull(message = "Username cannot be null.")
    @NotEmpty(message = "Username cannot be empty.")
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @NotNull(message = "Password cannot be null.")
    @NotEmpty(message = "Password cannot be empty.")
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotNull(message = "Password cannot be null.")
    @NotEmpty(message = "Password cannot be empty.")
    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
