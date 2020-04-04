package ru.job4j.parser;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Ramil Bischev
 */
public class Parser implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        if (TimeOfLastRun.getDate() == null) {
            TimeOfLastRun.setDate(LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0));
        } else {
            TimeOfLastRun.setDate(LocalDateTime.now());
        }

        Config parserConfig = new Config();
        parserConfig.init();
        ParseURL parser = new ParseURL();
        new RecordToDB(parserConfig).writeRecords(parser.parseURL(parserConfig.get("parseurl")));
    }
}
