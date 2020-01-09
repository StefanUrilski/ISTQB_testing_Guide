package com.example.exam.util;

public interface FileReader {

    String INVALID_READ_WRITE_FILE_NAME_MESSAGE = "Oops, something goes wrong... ";

    String QUESTION_TEXT_FILE_NAME = "Sample Exam - Question & Answer Set.txt";

    String readFile(String fileName);
}
