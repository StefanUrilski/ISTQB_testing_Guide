package com.example.exam.service;

import com.example.exam.domain.entities.AddedFiles;
import com.example.exam.domain.entities.Question;
import com.example.exam.domain.models.service.question.QuestionFilesInfoServiceModel;
import com.example.exam.domain.models.service.question.QuestionFilesServiceModel;
import com.example.exam.domain.models.service.question.QuestionInfoServiceModel;
import com.example.exam.domain.models.service.UserServiceModel;
import com.example.exam.errors.QuestionSetFailureException;
import com.example.exam.factory.QuestionFactory;
import com.example.exam.repository.AddedFilesRepository;
import com.example.exam.repository.QuestionRepository;
import com.example.exam.util.FileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.exam.common.Constants.QUESTION_ADDING_EXCEPTION;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final String TEXT_FILES_FOLDER_PATH = String.format("%s\\src\\main\\resources\\static\\textFiles",
            System.getProperty("user.dir"));

    private final FileReader fileReader;
    private final QuestionFactory questionFactory;
    private final QuestionRepository questionRepository;
    private final AddedFilesRepository addedFilesRepository;
    private final UserService userService;

    @Autowired
    public QuestionServiceImpl(FileReader fileReader,
                               QuestionFactory questionFactory,
                               QuestionRepository questionRepository,
                               AddedFilesRepository addedFilesRepository,
                               UserService userService) {
        this.fileReader = fileReader;
        this.questionFactory = questionFactory;
        this.questionRepository = questionRepository;
        this.addedFilesRepository = addedFilesRepository;
        this.userService = userService;
    }

    private int calcPercentage(int allQuestions, int userVisitedQuestionCount) {
        return (userVisitedQuestionCount / allQuestions) * 100;
    }

    private List<String> getExistedFilesNames() {
        File file = new File(TEXT_FILES_FOLDER_PATH);
        String[] fileList = file.list();

        if (fileList == null) {
            return new ArrayList<>();
        }

        return Arrays.asList(fileList);
    }


    @Override
    public void saveTextFileDataToDB(String fileName) {
        String questionSet = fileName.substring(0,1);

        String[] inputFileQuestions = fileReader.readFile(fileName)
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

    @Override
    public QuestionFilesServiceModel getFilesNames() {
        List<String> addedFiles = addedFilesRepository.findAll().stream()
                .map(AddedFiles::getAddedFileName)
                .collect(Collectors.toList());

        List<String> existedFiles = getExistedFilesNames();

        List<QuestionFilesInfoServiceModel> fileNames =
                existedFiles.stream()
                .map(existedFileName -> {
                    QuestionFilesInfoServiceModel file = new QuestionFilesInfoServiceModel();
                    file.setFileName(existedFileName);

                    if (addedFiles.contains(existedFileName)) {
                        file.setAdded(true);
                    }
                    return file;
                })
                .collect(Collectors.toList());

        return new QuestionFilesServiceModel(fileNames);
    }

}
