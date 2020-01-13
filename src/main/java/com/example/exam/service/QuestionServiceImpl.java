package com.example.exam.service;

import com.example.exam.domain.entities.Question;
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

    @Autowired
    public QuestionServiceImpl(FileReader fileReader,
                               QuestionFactory questionFactory,
                               QuestionRepository questionRepository) {
        this.fileReader = fileReader;
        this.questionFactory = questionFactory;
        this.questionRepository = questionRepository;
    }

    @Override
    public void saveTextFileDataToDB(String fileName) {
        String fileNameWithExtension = String.format("%s.txt", fileName);

        String[] inputFileQuestions = fileReader.readFile(fileNameWithExtension)
                .split(QUESTION_DELIMITER_REGEX);

        List<Question> questions = Arrays.stream(inputFileQuestions)
                .map(
                        questionFactory::buildQuestion
                )
                .collect(Collectors.toList());

        try {
            questionRepository.saveAll(questions);
        } catch (Exception e) {
            throw new QuestionSetFailureException(QUESTION_ADDING_EXCEPTION);
        }
    }
}
