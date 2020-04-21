package ru.job4j.parser;

import java.util.List;
import java.util.function.Predicate;

public interface Store {

    void save(List<Post> post);

    List<Post> get(Predicate<Post> filter);
}
