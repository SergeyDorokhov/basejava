package ru.topjava.basejava.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamWork {

    public static int minValue(int[] values) {
        return Arrays.stream(values).distinct().sorted().
                reduce((s1, s2) -> Integer.parseInt(s1 + "" + s2)).getAsInt();
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        return (integers.stream().mapToInt(s -> s).sum() % 2 == 0)
                ? getCollect(integers, s -> s % 2 != 0)
                : getCollect(integers, s -> s % 2 == 0);
    }

    private static List<Integer> getCollect(List<Integer> integers, Predicate<? super Integer> predicate) {
        return integers.stream().filter(predicate).collect(Collectors.toList());
    }
}
