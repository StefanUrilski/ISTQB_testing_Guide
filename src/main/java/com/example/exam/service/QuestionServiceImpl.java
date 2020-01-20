package com.example.exam.service;

import com.example.exam.domain.entities.AddedFiles;
import com.example.exam.domain.entities.Question;
import com.example.exam.domain.entities.User;
import com.example.exam.domain.models.binding.TestAnswerBindingModel;
import com.example.exam.domain.models.binding.TestBindingModel;
import com.example.exam.domain.models.service.ResultQuestsServiceModel;
import com.example.exam.domain.models.service.TestAnswerServiceModel;
import com.example.exam.domain.models.service.question.*;
import com.example.exam.errors.FileAlreadyExistsException;
import com.example.exam.errors.QuestionSetFailureException;
import com.example.exam.factory.QuestionFactory;
import com.example.exam.repository.AddedFilesRepository;
import com.example.exam.repository.FigureRepository;
import com.example.exam.repository.QuestionRepository;
import com.example.exam.util.FileReader;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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

    @Autowired
    public QuestionServiceImpl(FileReader fileReader,
                               QuestionFactory questionFactory,
                               QuestionRepository questionRepository,
                               AddedFilesRepository addedFilesRepository,
                               FigureRepository figureRepository,
                               UserService userService,
                               ModelMapper modelMapper) {
        this.fileReader = fileReader;
        this.questionFactory = questionFactory;
        this.questionRepository = questionRepository;
        this.addedFilesRepository = addedFilesRepository;
        this.figureRepository = figureRepository;
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

    private List<FigureServiceModel> getAllFiguresAsServiceModel() {
        return figureRepository.findAll().stream()
                .map(figure -> modelMapper.map(figure, FigureServiceModel.class))
                .collect(Collectors.toList());
    }

    private List<TestAnswerBindingModel> getTestAnswers(TestBindingModel testAnswers) {
        return new ArrayList<>() {{
            add(new TestAnswerBindingModel(testAnswers.getAnswerSymbol0(), testAnswers.getQuestionId0()));
            add(new TestAnswerBindingModel(testAnswers.getAnswerSymbol1(), testAnswers.getQuestionId1()));
            add(new TestAnswerBindingModel(testAnswers.getAnswerSymbol2(), testAnswers.getQuestionId2()));
            add(new TestAnswerBindingModel(testAnswers.getAnswerSymbol3(), testAnswers.getQuestionId3()));
            add(new TestAnswerBindingModel(testAnswers.getAnswerSymbol4(), testAnswers.getQuestionId4()));
            add(new TestAnswerBindingModel(testAnswers.getAnswerSymbol5(), testAnswers.getQuestionId5()));
            add(new TestAnswerBindingModel(testAnswers.getAnswerSymbol6(), testAnswers.getQuestionId6()));
            add(new TestAnswerBindingModel(testAnswers.getAnswerSymbol7(), testAnswers.getQuestionId7()));
            add(new TestAnswerBindingModel(testAnswers.getAnswerSymbol8(), testAnswers.getQuestionId8()));
            add(new TestAnswerBindingModel(testAnswers.getAnswerSymbol9(), testAnswers.getQuestionId9()));
        }};
    }

    private List<AnswerServiceModel> orderAnswers(List<AnswerServiceModel> answers) {
        return answers.stream()
                .sorted(Comparator.comparingInt(AnswerServiceModel::getSymbol))
                .collect(Collectors.toList());
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

    @Override
    public ResultQuestsServiceModel checkAnswers(TestBindingModel testAnswers) {
        Map<Long, Character> answers = new HashMap<>();

        getTestAnswers(testAnswers)
                .forEach(testAnswer -> answers.put(testAnswer.getQuestionId(), testAnswer.getAnswerSymbol()));

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


        ResultQuestsServiceModel result = new ResultQuestsServiceModel();
        result.setCorrectAnswers(correctAnswers);
        result.setTables(getAllFiguresAsServiceModel());
        return result;
    }

}
