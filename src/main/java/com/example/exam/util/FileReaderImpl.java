package com.example.exam.util;

import com.example.exam.errors.FileNotExistException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.example.exam.common.Constants.INVALID_READ_WRITE_FILE_NAME_MESSAGE;

@Component
public class FileReaderImpl implements FileReader {


    @Override
    public String readFile(String fileName) {
        String filePath = String.format("%s\\src\\main\\resources\\static\\textFiles\\%s",
                System.getProperty("user.dir"), fileName);

        List<String> allLines;
        try {
            allLines = Files.readAllLines(Path.of(filePath));
        } catch (IOException e) {
            throw new FileNotExistException(INVALID_READ_WRITE_FILE_NAME_MESSAGE);
        }

        return String.join(System.lineSeparator(), allLines);
    }
}
