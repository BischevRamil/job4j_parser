package ru.job4j.parser;

import org.apache.log4j.Logger;
import java.sql.*;
import java.util.List;

/**
 * @author Ramil Bischev
 * Класс записи вакансий в БД PostgreSQL.
 */
public class RecordToDB implements AutoCloseable {
    private Connection connection;
    private Config config;
    final static Logger LOGGER = Logger.getLogger(UsageLog4j.class);

    public RecordToDB(Config config) {
        this.config = config;
    }

    public boolean connectToDB() {
        try {
            Class.forName(config.get("driver-class-name"));
            this.connection = DriverManager.getConnection(
                    config.get("url"),
                    config.get("username"),
                    config.get("password")
            );
            this.connection.setAutoCommit(false);
            Statement st = connection.createStatement();
            st.execute("create table if not exists vacancies (id serial primary key, name varchar(1000) not null, text varchar(5000), link varchar(500) not null, date timestamp not null);");
            this.connection.commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return this.connection != null;
    }

    public void writeRecords(List<Vacancy> vacancyList) {
        if (connectToDB()) {
            try (PreparedStatement statement = connection.prepareStatement(SQLItems.INSERT.query)) {
                for (Vacancy vc : vacancyList) {
                    statement.setString(1, vc.getName());
                    statement.setString(2, vc.getText());
                    statement.setString(3, vc.getLink());
                    statement.setObject(4, vc.getDate());
                    if (!this.isDublicate(vc.getName())) {
                        statement.addBatch();
                    }
                }
                statement.executeBatch();
                this.connection.commit();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private boolean isDublicate(String name) {
        boolean result = false;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM vacancies;");
            while (resultSet.next()) {
                if (resultSet.getString("name").equals(name)) {
                    result = true;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        this.connection.close();
    }

    enum SQLItems {
        INSERT("INSERT INTO vacancies (name, text, link, date) VALUES (?, ?, ?, ?);");

        String query;

        SQLItems(String query) {
            this.query = query;
        }
    }
}
