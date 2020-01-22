package com.example.exam.util;

import java.util.Set;

public interface RandomProvider {

    /**
     * Returns set of ten unique random integers in range from between zero (inclusive)
     * and {@code maxNumber} (exclusive). Uses random number generator, from
     * Random class. Method made only for testing purposes.
     *
     * @param maxNumber the upper bound (exclusive).  Must be positive.
     * @return list of ten random integers in range from between zero (inclusive)
     * and {@code maxNumber} (exclusive).
     * @throws IllegalArgumentException if bound is not positive (from Random class).
     */
    Set<Integer> getTenUniqueRandomNumbers(int maxNumber);
}
