package com.example.exam.service;

import com.example.exam.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void registerUser(UserServiceModel userServiceModel);

    UserServiceModel getUserByName(String name);

    void updateVisitedQuestions(UserServiceModel userServiceModel, String visitedQuestions);

    void updateUser(String username);
}
