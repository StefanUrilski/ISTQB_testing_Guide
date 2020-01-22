package com.example.exam.util;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RandomProviderImpl implements RandomProvider {

    @Override
    public Set<Integer> getTenUniqueRandomNumbers(int maxNumber) {
        Set<Integer> randomNumbers = new HashSet<>();

        while (randomNumbers.size() != 10) {
            randomNumbers.add(new Random().nextInt(maxNumber));
        }

        return randomNumbers;
    }
}
