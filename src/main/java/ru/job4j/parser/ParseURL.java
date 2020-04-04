package ru.job4j.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Bischev Ramil
 * Класс парсинга вакансий JAVA-разработчика сайта SQL.RU
 */
public class ParseURL {
    private List<Vacancy> vacancyList = new ArrayList<>();
    private boolean stopParse = false;
    final static Logger LOGGER = Logger.getLogger(UsageLog4j.class);

    public List<Vacancy> parseURL(String url) {
        int i = 1;
        while (!stopParse) {
            parsePage(url + "/" + i);
            i++;
        }
        return this.vacancyList;
    }

    private void parsePage(String urlPage) {
        try {
            Document doc = Jsoup.connect(urlPage).get();
            Elements forumTable = doc.select("table.forumTable");
            forumTable.select("tr").forEach(element -> {
                if (!stopParse) {
                    if (isJava(element.select("td[class=postslisttopic]").text())) {
                        String name = element.select("td[class=postslisttopic]").text().trim();
                        String link = element.select("a").attr("href").trim();
                        String text = parseLink(link).trim();
                        String date = element.select("td[class=altCol]").last().text();
                        LocalDateTime localDateTime = parseDateString(date);
                        if (localDateTime.isAfter(TimeOfLastRun.getDate())) {
                            this.vacancyList.add(new Vacancy(name, text, link, localDateTime));
                        } else {
                            stopParse = true;
                            LOGGER.info("стоп парсинг");
                        }
                    }
                }
            });
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }


    /**
     * Метод вытаскивания описания вакансии из ссылки
     * @param urlLink ссылка на описание
     * @return описание вакансии
     */
    private String parseLink(String urlLink) {
        String text = "";
        try {
            Document doc = Jsoup.connect(urlLink).get();
            Element msgTable = doc.select("table.msgTable").first();
            text =  msgTable.select("td.msgBody").last().text().trim();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return text;
    }


    private boolean isJava(String name) {
        return (name.contains("Java") || name.contains("java") || name.contains("JAVA"))
                && !(name.contains("Script") || name.contains("script") || name.contains("SCRIPT"));
    }

    /**
     * Метод конвертации строки даты в LocaleDateTime
     * @param dateString строка даты
     * @return LocaleDateTime
     */
    private static LocalDateTime parseDateString(String dateString) {
        String[] dateTimeArr = dateString.trim().split(",");
        String date = dateTimeArr[0];
        String[] dateArr = date.trim().split(" ");
        String[] timeArr = dateTimeArr[1].trim().split(":");
        int hours = Integer.parseInt(timeArr[0]);
        int minutes = Integer.parseInt(timeArr[1]);
        LocalDateTime resultDate = null;

        if (dateString.contains("сегодня")) {
            resultDate = LocalDateTime.of(LocalDate.now(), LocalTime.of(hours, minutes));
        } else if (dateString.contains("вчера")) {
            resultDate = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(hours, minutes));
        } else {
            int year = Integer.parseInt("20" + dateArr[2]);
            int mouth = getMonthByName(dateArr[1]);
            int day = Integer.parseInt(dateArr[0]);
            resultDate = LocalDateTime.of(year, mouth, day, hours, minutes);
        }
        return resultDate;
    }

    private static int getMonthByName(String month) {
        int numMonth;
        switch (month) {
            case ("янв") : numMonth = 1; break;
            case ("фев") : numMonth = 2; break;
            case ("мар") : numMonth = 3; break;
            case ("апр") : numMonth = 4; break;
            case ("май") : numMonth = 5; break;
            case ("июн") : numMonth = 6; break;
            case ("июл") : numMonth = 7; break;
            case ("авг") : numMonth = 8; break;
            case ("сен") : numMonth = 9; break;
            case ("отк") : numMonth = 10; break;
            case ("ноя") : numMonth = 11; break;
            case ("дек") : numMonth = 12; break;
            default: numMonth = -1;
        }
        return numMonth;
    }

}
