package ru.job4j.parser;

import java.util.List;

/**
 * list(link) - этот метод загружает список объявлений по ссылке типа - https://www.sql.ru/forum/job-offers/1
 * detail(link) - этот метод загружает детали объявления по ссылке типа - https://www.sql.ru/forum/1323839/razrabotchik-java-g-kazan
 */
public interface Parse {
    List<Post> list(String link);

    void detail(String link);
}
