package com.example.exam.service;

import com.example.exam.domain.entities.AddedFiles;
import com.example.exam.domain.entities.Question;
import com.example.exam.domain.entities.User;
import com.example.exam.domain.models.service.question.*;
import com.example.exam.errors.FileAlreadyExistsException;
import com.example.exam.errors.QuestionSetFailureException;
import com.example.exam.factory.QuestionFactory;
import com.example.exam.repository.AddedFilesRepository;
import com.example.exam.repository.QuestionRepository;
import com.example.exam.util.FileReader;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.example.exam.common.Constants.*;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final String TEXT_FILES_FOLDER_PATH = String.format("%s\\src\\main\\resources\\static\\textFiles",
            System.getProperty("user.dir"));

    private final FileReader fileReader;
    private final QuestionFactory questionFactory;
    private final QuestionRepository questionRepository;
    private final AddedFilesRepository addedFilesRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public QuestionServiceImpl(FileReader fileReader,
                               QuestionFactory questionFactory,
                               QuestionRepository questionRepository,
                               AddedFilesRepository addedFilesRepository,
                               UserService userService,
                               ModelMapper modelMapper) {
        this.fileReader = fileReader;
        this.questionFactory = questionFactory;
        this.questionRepository = questionRepository;
        this.addedFilesRepository = addedFilesRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
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

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> unique = ConcurrentHashMap.newKeySet();
        return t -> unique.add(keyExtractor.apply(t));
    }

    private void checkQuestionSetIdAndIfFailsThrowException(String[] tokens, int size) {
        if (tokens.length != 2 || size == 0) {
            throw new QuestionSetFailureException(QUESTION_DOES_NOT_EXISTS);
        }

        boolean isNumber = tokens[1].chars().allMatch(Character::isDigit);

        if (!isNumber) {
            throw new QuestionSetFailureException(QUESTION_DOES_NOT_EXISTS);
        }
    }

    private List<Question> getQuestionsSet(String questionSetId) {
        String[] tokens = questionSetId.split("Q");
        String questionSet = tokens[0];
        List<Question> questions = questionRepository.findAllByQuestionSetOrderById(questionSet);

        checkQuestionSetIdAndIfFailsThrowException(tokens, questions.size());

        int maxQuestions = Integer.parseInt(tokens[1]);

        return questions.subList(maxQuestions - 10, maxQuestions);
    }

    @Override
    public void saveTextFileDataToDB(String fileName) {
        AddedFiles existingFileName = addedFilesRepository.findByAddedFileName(fileName);

        if (existingFileName != null) {
            throw new FileAlreadyExistsException(FILE_ALREADY_EXISTS);
        }

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
            addedFilesRepository.save(new AddedFiles(fileName));
        } catch (Exception e) {
            throw new QuestionSetFailureException(QUESTION_ADDING_EXCEPTION);
        }
    }

    @Override
    public QuestionInfoServiceModel getQuestionsInfo(String username) {
        User user = modelMapper.map(userService.getUserByName(username), User.class);
        List<Question> allQuestions = questionRepository.findAll();

        if (allQuestions.size() == 0) return null;

        int userVisitedQuestionCount = (int) allQuestions.stream()
                .filter(question -> question.getUsers().contains(user))
                .count();

        int allQuestionSetsNumber = (int) allQuestions.stream()
                .filter(distinctByKey(Question::getQuestionSet))
                .count();


        int percentage = calcPercentage(allQuestions.size(), userVisitedQuestionCount);

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

    @Override
    public QuestionsSetServiceModel getQuestionsByQuestionSetId(String questionSetId) {
        List<Question> allQuestionsBySet = getQuestionsSet(questionSetId);

        List<QuestionAskedServiceModel> askedQuestions = allQuestionsBySet.stream()
                .map(question -> modelMapper.map(question, QuestionAskedServiceModel.class))
                .collect(Collectors.toList());

        QuestionsSetServiceModel questionSet = new QuestionsSetServiceModel();
        questionSet.setQuestions(askedQuestions);
        questionSet.setQuestionSetId(questionSetId);

        return questionSet;
    }


}
