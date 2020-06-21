package ru.topjava.basejava.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamWork {

    public static int minValue(int[] values) {
        return Arrays.stream(values).distinct().sorted().reduce(0, (x, y) -> x * 10 + y);
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        boolean isEven = integers.stream().mapToInt(s -> s).sum() % 2 == 0;
        return integers.stream().filter(o -> isEven ^ (o % 2 == 0)).collect(Collectors.toList());
    }
}
