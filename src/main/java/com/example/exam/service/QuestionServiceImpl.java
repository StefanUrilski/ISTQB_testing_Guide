package com.example.exam.service;

import com.example.exam.domain.entities.AddedFiles;
import com.example.exam.domain.entities.Question;
import com.example.exam.domain.models.binding.TestBindingModel;
import com.example.exam.domain.models.service.ResultQuestsServiceModel;
import com.example.exam.domain.models.service.TestAnswerServiceModel;
import com.example.exam.domain.models.service.UserServiceModel;
import com.example.exam.domain.models.service.question.*;
import com.example.exam.errors.AllQuestionVisitedException;
import com.example.exam.errors.FileAlreadyExistsException;
import com.example.exam.errors.QuestionSetFailureException;
import com.example.exam.factory.QuestionFactory;
import com.example.exam.repository.AddedFilesRepository;
import com.example.exam.repository.FigureRepository;
import com.example.exam.repository.QuestionRepository;
import com.example.exam.util.FileReader;
import com.example.exam.util.RandomProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
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
    private final FigureRepository figureRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final RandomProvider randomProvider;

    @Autowired
    public QuestionServiceImpl(FileReader fileReader,
                               QuestionFactory questionFactory,
                               QuestionRepository questionRepository,
                               AddedFilesRepository addedFilesRepository,
                               FigureRepository figureRepository,
                               UserService userService,
                               ModelMapper modelMapper,
                               RandomProvider randomProvider) {
        this.fileReader = fileReader;
        this.questionFactory = questionFactory;
        this.questionRepository = questionRepository;
        this.addedFilesRepository = addedFilesRepository;
        this.figureRepository = figureRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.randomProvider = randomProvider;
    }

    private double calcPercentage(int allQuestions, int userVisitedQuestionCount) {
        return  ((double) userVisitedQuestionCount / (double) allQuestions) * 100;
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

        if (maxQuestions > questions.size()) {
            throw new QuestionSetFailureException(QUESTION_DOES_NOT_EXISTS);
        }

        return questions.subList(maxQuestions - 10, maxQuestions);
    }

    private List<FigureServiceModel> getAllFiguresAsServiceModel() {
        return figureRepository.findAll().stream()
                .map(figure -> modelMapper.map(figure, FigureServiceModel.class))
                .collect(Collectors.toList());
    }

    private Map<Long, Character> getTestAnswers(TestBindingModel testBindingModel) {
        Map<Long, Character> testAnswers = new LinkedHashMap<>();
        Field[] fields = testBindingModel.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i+=2) {
            Field field1 = fields[i];
            Field field2 = fields[i + 1];
            field1.setAccessible(true);
            field2.setAccessible(true);

            Character answerSymbol = null;
            Long questionId = null;
            try {
                answerSymbol = (char) field1.get(testBindingModel);
                questionId = (Long) field2.get(testBindingModel);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (questionId != null) {
                testAnswers.put(questionId, answerSymbol);
            }
        }

        return testAnswers;
    }

    private List<AnswerServiceModel> orderAnswers(List<AnswerServiceModel> answers) {
        return answers.stream()
                .sorted(Comparator.comparingInt(AnswerServiceModel::getSymbol))
                .collect(Collectors.toList());
    }

    private QuestionsSetServiceModel getQuestionsSetServiceModel(String questionSetId, List<Question> allQuestionsBySet) {
        List<QuestionServiceModel> askedQuestions = allQuestionsBySet.stream()
                .map(question -> {
                    QuestionServiceModel model = modelMapper.map(question, QuestionServiceModel.class);
                    model.setAnswers(orderAnswers(model.getAnswers()));

                    return model;
                })
                .collect(Collectors.toList());

        QuestionsSetServiceModel questionSet = new QuestionsSetServiceModel();
        questionSet.setQuestions(askedQuestions);
        questionSet.setQuestionSetId(questionSetId);
        questionSet.setTables(getAllFiguresAsServiceModel());
        return questionSet;
    }

    private List<Question> getTenRandomQuestions(List<Question> questions, int count) {
        List<Question> randomQuestions = new ArrayList<>();

        randomProvider.getUniqueRandomNumbers(questions.size(), count)
                .forEach(randomNumber -> randomQuestions.add(questions.get(randomNumber)));

        return randomQuestions;
    }

    private boolean containsQuestion(Long questionId, Set<Long> questions) {
        for (Long userQuestionId : questions) {
            if (userQuestionId.equals(questionId)) {
                return true;
            }
        }
        return false;
    }

    private boolean notContainQuestion(Long questionId, Set<Long> questions) {
        for (Long userQuestionId : questions) {
            if (userQuestionId.equals(questionId)) {
                return false;
            }
        }
        return true;
    }

    private List<Question> getRandomQuestionsByUser(String username, int questionsCount) {
        UserServiceModel user = userService.getUserByName(username);

        List<Question> unseenQuestions = questionRepository.findAll().stream()
                .filter(question -> notContainQuestion(question.getId(), user.getVisitedQuestions()))
                .collect(Collectors.toList());

        if (unseenQuestions.size() < questionsCount) {
            throw new AllQuestionVisitedException();
        }

        if (unseenQuestions.size() != questionsCount) {
            unseenQuestions = getTenRandomQuestions(unseenQuestions, questionsCount);
        }
        String visitedQuestions = unseenQuestions.stream()
                .map(question -> Long.toString(question.getId()))
                .collect(Collectors.joining(", "));

        userService.updateVisitedQuestions(user, visitedQuestions);
        return unseenQuestions;
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
        UserServiceModel user = userService.getUserByName(username);
        List<Question> allQuestions = questionRepository.findAll();

        if (allQuestions.size() == 0) return null;

        int userVisitedQuestionCount = (int) allQuestions.stream()
                .filter(question -> containsQuestion(question.getId(), user.getVisitedQuestions()))
                .count();

        int allQuestionSetsNumber = (int) allQuestions.stream()
                .filter(distinctByKey(Question::getQuestionSet))
                .count();


        int percentage = (int) calcPercentage(allQuestions.size(), userVisitedQuestionCount);

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

        return getQuestionsSetServiceModel(questionSetId, allQuestionsBySet);
    }

    @Override
    public ResultQuestsServiceModel checkAnswers(TestBindingModel testAnswers) {
        Map<Long, Character> answers = getTestAnswers(testAnswers);

        List<TestAnswerServiceModel> correctAnswers = questionRepository.findAll().stream()
                .filter(question -> answers.get(question.getId()) != null)
                .map(question -> {
                    TestAnswerServiceModel model = modelMapper.map(question, TestAnswerServiceModel.class);
                    model.setAnswers(orderAnswers(model.getAnswers()));

                    boolean isAnswerCorrect = answers.get(question.getId()).equals(question.getCorrectAnswer());
                    model.setValid(isAnswerCorrect);

                    return model;
                })
                .collect(Collectors.toList());

        int score = (int) correctAnswers.stream().filter(TestAnswerServiceModel::isValid).count();
        int percentages = (int) calcPercentage(answers.size(), score);

        ResultQuestsServiceModel result = new ResultQuestsServiceModel();
        result.setCorrectAnswers(correctAnswers);
        result.setTables(getAllFiguresAsServiceModel());
        result.setScorePoints(String.format("%s out of %s", score, answers.size()));
        result.setPercentages(percentages);
        return result;
    }

    @Override
    public QuestionsSetServiceModel getTenRandomQuestionByUser(String username) {
        List<Question> unseenQuestions = getRandomQuestionsByUser(username, 10);

        return getQuestionsSetServiceModel("Random", unseenQuestions);
    }

    @Override
    public QuestionsSetServiceModel getFortyRandomQuestionByUser(String username) {
        List<Question> unseenQuestions = getRandomQuestionsByUser(username, 40);

        return getQuestionsSetServiceModel("Random", unseenQuestions);
    }

    @Override
    public void startOver(String username) {
        userService.updateUser(username);
    }
}

