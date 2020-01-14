package com.example.exam.service;

import com.example.exam.domain.entities.Question;
import com.example.exam.domain.models.service.QuestionInfoServiceModel;
import com.example.exam.domain.models.service.UserServiceModel;
import com.example.exam.errors.QuestionSetFailureException;
import com.example.exam.factory.QuestionFactory;
import com.example.exam.repository.QuestionRepository;
import com.example.exam.util.FileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.exam.common.Constants.QUESTION_ADDING_EXCEPTION;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final FileReader fileReader;
    private final QuestionFactory questionFactory;
    private final QuestionRepository questionRepository;
    private final UserService userService;

    @Autowired
    public QuestionServiceImpl(FileReader fileReader,
                               QuestionFactory questionFactory,
                               QuestionRepository questionRepository,
                               UserService userService) {
        this.fileReader = fileReader;
        this.questionFactory = questionFactory;
        this.questionRepository = questionRepository;
        this.userService = userService;
    }

    private int calcPercentage(int allQuestions, int userVisitedQuestionCount) {
        return (userVisitedQuestionCount / allQuestions) * 100;
    }


    @Override
    public void saveTextFileDataToDB(String fileName) {
        String fileNameWithExtension = String.format("%s.txt", fileName);
        String questionSet = fileName.substring(0,1);

        String[] inputFileQuestions = fileReader.readFile(fileNameWithExtension)
                .split(QUESTION_DELIMITER_REGEX);

        List<Question> questions = Arrays.stream(inputFileQuestions)
                .map(questionWithAnswer -> {
                    Question question = questionFactory.buildQuestion(questionWithAnswer);
                    question.setQuestionSet(questionSet);

                    return question;
                })
                .collect(Collectors.toList());

        try {
            questionRepository.saveAll(questions);
        } catch (Exception e) {
            throw new QuestionSetFailureException(QUESTION_ADDING_EXCEPTION);
        }
    }

    @Override
    public QuestionInfoServiceModel getQuestionsInfo(String username) {
        UserServiceModel user = userService.getUserByName(username);
        int userVisitedQuestionCount = questionRepository.findAllVisitedQuestionsForUser(user.getId());
        int allQuestions = (int) questionRepository.count();

        int allQuestionSetsNumber = questionRepository.findAllQuestionSetsNumber();
        int percentage = calcPercentage(allQuestions, userVisitedQuestionCount);

        return new QuestionInfoServiceModel(allQuestionSetsNumber, percentage);
    }

}
