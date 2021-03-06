package com.example.exam.web.controllers;

import com.example.exam.domain.models.binding.TestBindingModel;
import com.example.exam.domain.models.service.question.QuestionInfoServiceModel;
import com.example.exam.domain.models.service.question.QuestionsSetServiceModel;
import com.example.exam.domain.models.view.ResultQuestsViewModel;
import com.example.exam.domain.models.view.question.QuestionFilesViewModel;
import com.example.exam.domain.models.view.question.QuestionInfoViewModel;
import com.example.exam.domain.models.view.question.QuestionsSetViewModel;
import com.example.exam.errors.AllQuestionVisitedException;
import com.example.exam.service.QuestionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;

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

    @GetMapping("/status")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView questionsFilesStatus() {
        QuestionFilesViewModel questionFilesNames =
                modelMapper.map(questionService.getFilesNames(), QuestionFilesViewModel.class);
        return view("questions/status-questions", "filesNames", questionFilesNames);
    }

    @GetMapping("/addFile/{fileName}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView addQuestionFile(@PathVariable String fileName) {
        questionService.saveTextFileDataToDB(fileName);

        return redirect("/questions/status");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER') && !hasRole('ROLE_ROOT')")
    public ModelAndView allQuestions(Principal principal) {
        QuestionInfoServiceModel questionsInfo = questionService.getQuestionsInfo(principal.getName());

        if (questionsInfo == null) {
            return view("questions/all-questions", "questions", null);
        }

        QuestionInfoViewModel questions = modelMapper.map(questionsInfo, QuestionInfoViewModel.class);

        return view("questions/all-questions", "questions", questions);
    }

    @GetMapping("/set/{questionSetId}")
    @PreAuthorize("hasRole('ROLE_USER') && !hasRole('ROLE_ROOT')")
    public ModelAndView questionsSet(@PathVariable String questionSetId) {
        QuestionsSetViewModel questions = modelMapper.map(
            questionService.getQuestionsByQuestionSetId(questionSetId),
            QuestionsSetViewModel.class
        );

        return view("questions/set-of-questions", "questions", questions);
    }

    @PostMapping("/set/exam")
    @PreAuthorize("hasRole('ROLE_USER') && !hasRole('ROLE_ROOT')")
    public ModelAndView questionsSetResults(@ModelAttribute TestBindingModel test) {
        ResultQuestsViewModel result = modelMapper.map(
                questionService.checkAnswers(test),
                ResultQuestsViewModel.class
        );

        return view("questions/set-of-answered-questions", "questions", result);
    }

    @GetMapping("/randomTen")
    @PreAuthorize("hasRole('ROLE_USER') && !hasRole('ROLE_ROOT')")
    public ModelAndView tenRandomQuestions(Principal principal) {
        QuestionsSetServiceModel randomQuestions;
        try {
            randomQuestions = questionService.getTenRandomQuestionByUser(principal.getName());
        } catch (AllQuestionVisitedException ex) {
            return  view("questions/no-random-questions");
        }

        QuestionsSetViewModel questions = modelMapper.map(randomQuestions, QuestionsSetViewModel.class);

        return view("questions/set-of-questions", "questions", questions);
    }

    @GetMapping("/randomForty")
    @PreAuthorize("hasRole('ROLE_USER') && !hasRole('ROLE_ROOT')")
    public ModelAndView fortyRandomQuestions(Principal principal) {
        QuestionsSetServiceModel randomQuestions;
        try {
            randomQuestions = questionService.getFortyRandomQuestionByUser(principal.getName());
        } catch (AllQuestionVisitedException ex) {
            return  view("questions/no-random-questions");
        }

        QuestionsSetViewModel questions = modelMapper.map(randomQuestions, QuestionsSetViewModel.class);

        return view("questions/set-of-questions", "questions", questions);
    }

    @GetMapping("/startOver")
    @PreAuthorize("hasRole('ROLE_USER') && !hasRole('ROLE_ROOT')")
    public ModelAndView startOver(Principal principal) {
        questionService.startOver(principal.getName());

        return redirect("/questions/all");
    }

    @GetMapping("/keepAlive")
    @ResponseBody
    public Object keepAlive() {
        return new HashMap<String, String>() {{put("Backend", "alive");}};
    }
}
