package ru.job4j.parser;

import java.time.LocalDateTime;

public class Post {
    private String name;
    private String text;
    private String link;
    private LocalDateTime date;

    public Post(String name, String text, String link, LocalDateTime date) {
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

    @Override
    public String toString() {
        return name + "\n" + link + "\n" + text + "\n" + date + "\n" + "#######################################";
    }
}
