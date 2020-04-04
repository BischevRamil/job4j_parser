package ru.job4j.parser;

import org.apache.log4j.Logger;

public class UsageLog4j {
    final static Logger LOGGER = Logger.getLogger(UsageLog4j.class);

    public static void main(String[] args) {
        LOGGER.trace("trace message");
        LOGGER.debug("debug message");
        LOGGER.info("info message");
        LOGGER.warn("warn message");
        LOGGER.error("error message");
    }
}
