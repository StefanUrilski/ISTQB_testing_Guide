package com.example.exam.web.controllers;

import com.example.exam.domain.models.view.QuestionInfoViewModel;
import com.example.exam.service.QuestionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/questions")
public class QuestionController extends BaseController {

    private final QuestionService questionService;
    private final ModelMapper modelMapper;

    @Autowired
    public QuestionController(QuestionService questionService,
                              ModelMapper modelMapper) {
        this.questionService = questionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("all")
    public ModelAndView allQuestions(Principal principal) {
        QuestionInfoViewModel questions = modelMapper
                .map(questionService.getQuestionsInfo(principal.getName()), QuestionInfoViewModel.class);

        return view("questions/all-questions", "questions", questions);
    }

}