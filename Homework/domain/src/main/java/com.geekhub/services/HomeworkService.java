package com.geekhub.services;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.exceptions.*;
import com.geekhub.models.*;
import com.geekhub.mylogger.*;
import com.geekhub.sources.HomeworkSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeworkService {
    private final MyLogger logger;
    private final HomeworkSource homeworkSource;
    final String INSERT_QUERY = "INSERT INTO homework (id, task, deadline) VALUES (:id, :name, :deadline)";
    final String GET_QUERY = "SELECT * from homework where id = :id";
    final String DELETE_QUERY = "DELETE from homework where id = :id";
    final String SHOW_ALL_QUERY = "SELECT * from homework";

    public HomeworkService(MyLogger logger, HomeworkSource homeworkSource) {
        this.logger = logger;
        this.homeworkSource = homeworkSource;
    }

    public HomeWork createHomework(String task, int deadline) throws FileNotFoundException {
        if (task.isBlank()) {
            throw new ValidationException("Validation exception!");
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String pattern = "MM-dd-yyyy HH:mm:ss:SS";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String format = zonedDateTime.plusDays(deadline).format(formatter);
        HomeWork homeWork = new HomeWork(task);
        homeworkSource.add(homeWork);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        Map<String, Object> params = new HashMap<>();
        int id = (int) (Math.random()*(600+1)) - 200;
        params.put("id", id);
        params.put("task", task);
        params.put("deadline", deadline);
        jdbcTemplate.update(INSERT_QUERY, params);
        logger.log(LoggerType.INFO, ResourseService.class, "You have added a new homework!");
        return homeWork;
    }

    public HomeWork getHomework(int homeworkIndex) throws FileNotFoundException {
        try {
            homeworkSource.get(homeworkIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        int id = homeworkIndex;
        HomeWork homeWork = jdbcTemplate.queryForObject(GET_QUERY, Map.of("id", id), (rs, rowNum) ->
                new HomeWork(
                        rs.getString("task")
                )
        );
        return homeworkSource.get(homeworkIndex);
    }

    public void deleteHomework(int homeworkIndex) throws FileNotFoundException {
        try {
            homeworkSource.delete(homeworkIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        jdbcTemplate.update(DELETE_QUERY, Map.of("id", homeworkIndex));
        logger.log(LoggerType.WARNING, ResourseService.class, "You have deleted a homework!");
    }

    public String showHomeworks() throws FileNotFoundException {
        String homeworks = new String("");
        for (int i = 0; i < homeworkSource.showHomeworks().size(); i++) {
            homeworks += homeworkSource.get(i + 1).getTask() +"\n";
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        List<HomeWork> list = jdbcTemplate.query(SHOW_ALL_QUERY, (rs, rowNum) -> {
            HomeWork homeWork = new HomeWork(
                    rs.getString("task"));
            homeWork.setId(rs.getInt("id"));
            return homeWork;
        });
        for (HomeWork homeWork : list) {
            System.out.println(homeWork.getId() + " " + homeWork.getTask());
        }
        return homeworks;
    }

    public void loadHomeworksToList() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        List<HomeWork> list = jdbcTemplate.query(SHOW_ALL_QUERY, (rs, rowNum) -> {
            HomeWork homeWork = new HomeWork(
                    rs.getString("task"));
            homeWork.setId(rs.getInt("id"));
            homeworkSource.add(homeWork);
            return homeWork;
        });
    }
}
