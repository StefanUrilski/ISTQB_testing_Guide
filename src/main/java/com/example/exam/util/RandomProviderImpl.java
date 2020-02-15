package com.example.exam.util;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RandomProviderImpl implements RandomProvider {

    @Override
    public Set<Integer> getUniqueRandomNumbers(int maxNumber, int count) {
        Set<Integer> randomNumbers = new HashSet<>();

        while (randomNumbers.size() != count) {
            randomNumbers.add(new Random().nextInt(maxNumber));
        }

        return randomNumbers;
    }
}
