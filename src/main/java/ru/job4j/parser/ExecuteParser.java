package ru.job4j.parser;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.time.LocalDateTime;

/**
 * @author Ramil Bischev
 */
public class ExecuteParser implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        if (TimeOfLastRun.getDate() == null) {
            TimeOfLastRun.setDate(LocalDateTime.of(LocalDateTime.now().getYear(), 1, 1, 0, 0));
        } else {
            TimeOfLastRun.setDate(LocalDateTime.now());
        }

        Config parserConfig = new Config();
        parserConfig.init();
        Parse parser = new SqlRuParse();
        new RecordToDB(parserConfig).save(parser.list(parserConfig.get("parseurl")));
    }
}
