package ru.job4j.parser;

import org.apache.log4j.Logger;
import java.sql.*;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Ramil Bischev
 * Save records to PostgreSQL.
 */
public class RecordToDB implements AutoCloseable, Store {
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

    /**
     * Save List of vacancies to database.
     * @param postList List of vacancies.
     */
    @Override
    public void save(List<Post> postList) {
        if (connectToDB()) {
            try (PreparedStatement statement = connection.prepareStatement(SQLItems.INSERT.query)) {
                for (Post vc : postList) {
                    statement.setString(1, vc.getName());
                    statement.setString(2, vc.getText());
                    statement.setString(3, vc.getLink());
                    statement.setObject(4, vc.getDate());
                    if (!this.isDuplicate(vc.getName())) {
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

    @Override
    public List<Post> get(Predicate<Post> filter) {
        return null;
    }

    /**
     * Check duplicate record in database.
     * @param name checking name.
     * @return true if name equals record.
     */
    private boolean isDuplicate(String name) {
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
