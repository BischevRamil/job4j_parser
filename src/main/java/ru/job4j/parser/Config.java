package ru.job4j.parser;

import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * @author Bischev Ramil
 */
public class Config {
    final static Logger LOGGER = Logger.getLogger(UsageLog4j.class);

    private final Properties values = new Properties();

    public void init() {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("parser.properties")) {
            values.load(in);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public String get(String key) {
        return this.values.getProperty(key);
    }
}
