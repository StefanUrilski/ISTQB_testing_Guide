package com.example.exam.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReaderImpl implements FileReader {


    @Override
    public String readFile(String fileName) {
        String filePath = String.format("%s\\src\\main\\resources\\static\\textFiles\\%s",
                System.getProperty("user.dir"), fileName);

        List<String> allLines;
        try {
            allLines = Files.readAllLines(Path.of(filePath));
        } catch (IOException e) {
            return INVALID_READ_WRITE_FILE_NAME_MESSAGE;
        }

        return String.join(System.lineSeparator(), allLines);
    }
}
