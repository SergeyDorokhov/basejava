package ru.topjava.basejava.util;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class StreamWorkTest {

    @Test
    public void minValueTest() {
        assertEquals(123, StreamWork.minValue(new int[]{1, 2, 3, 3, 2, 3}));
    }

    @Test
    public void oddCheckTest() {
        assertEquals(Arrays.asList(2, 4), StreamWork.oddOrEven(Arrays.asList(1, 2, 4)));
    }

    @Test
    public void evenCheckTest() {
        assertEquals(Arrays.asList(1, 3), StreamWork.oddOrEven(Arrays.asList(1, 2, 3)));
    }
}