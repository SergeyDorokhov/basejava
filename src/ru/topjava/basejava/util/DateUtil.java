package ru.topjava.basejava.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static String format(LocalDate localDate) {
        if (localDate == null) {
            return "";
        }
        return localDate.format(formatter);
    }

    public static LocalDate parse(String date) {
        YearMonth yearMonth = YearMonth.parse(date, formatter);
        return LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
    }
}
