package ru.job4j.parser;

import java.time.LocalDateTime;

public class Vacancy {
    private String name;
    private String text;
    private String link;
    private LocalDateTime date;

    public Vacancy(String name, String text, String link, LocalDateTime date) {
        this.name = name;
        this.text = text;
        this.link = link;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }

    public LocalDateTime getDate() {
        return this.date;
    }
}
