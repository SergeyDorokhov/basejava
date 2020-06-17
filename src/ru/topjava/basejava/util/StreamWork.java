package ru.topjava.basejava.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamWork {

    public static int minValue(int[] values) {
        int[] sortedValues = Arrays.stream(values).distinct().sorted().toArray();
        int sum = 0;
        int degree = sortedValues.length - 1;
        int multiplier;
        for (int value : sortedValues) {
            multiplier = (int) Math.pow(10, degree--);
            sum += value * multiplier;
        }
        return sum;
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        return (integers.stream().mapToInt(s -> s).sum() % 2 == 0)
                ? integers.stream().filter(s -> s % 2 != 0).collect(Collectors.toList())
                : integers.stream().filter(s -> s % 2 == 0).collect(Collectors.toList());
    }
}
