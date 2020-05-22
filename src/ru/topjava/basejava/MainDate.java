package ru.topjava.basejava;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MainDate {
    public static void main(String[] args) {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        LocalDateTime ldt = LocalDateTime.of(ld, lt);
        System.out.println(ldt);
    }
}
