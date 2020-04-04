package ru.job4j.parser;

import java.time.LocalDateTime;

public class TimeOfLastRun {
    private static LocalDateTime date;

    public static LocalDateTime getDate() {
        return date;
    }

    public static void setDate(LocalDateTime date) {
        TimeOfLastRun.date = date;
    }
}
