package com.example.exam.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class RandomProviderImpl implements RandomProvider {

    @Override
    public List<Integer> getTenRandomNumbers(int maxNumber) {
        List<Integer> randomNumbers = new ArrayList<>();

        IntStream.range(0, 10)
                .forEach(number ->
                        randomNumbers.add(
                                new Random().nextInt(maxNumber)
                        )
                );

        return randomNumbers;
    }
}
