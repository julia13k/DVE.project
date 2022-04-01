package com.geekhub.services;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.exceptions.*;
import com.geekhub.models.*;
import com.geekhub.mylogger.*;
import com.geekhub.sources.ResourseSource;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourseService {
    private final MyLogger logger;
    private final ResourseSource resourseSource;
    final String INSERT_QUERY = "INSERT INTO resourse (id, resourse_name, data, type) " +
            "VALUES (:id, :resourse_name, :data, :type)";
    final String GET_QUERY = "SELECT * from resourse where id = :id";
    final String DELETE_QUERY = "DELETE from resourse where id = :id";
    final String SHOW_ALL_QUERY = "SELECT * from resourse";

    public ResourseService(MyLogger logger, ResourseSource resourseSource) {
        this.logger = logger;
        this.resourseSource = resourseSource;
    }

    public Resourse createResourse(String name, String data, String typeString) throws FileNotFoundException {
        if (name.isBlank() || data.isBlank() || typeString.isBlank()) {
            throw new ValidationException("Validation exception!");
        }

        if(!(typeString.equals(String.valueOf(ResourseType.BOOK)) &&
                !typeString.equals(String.valueOf(ResourseType.URL)) &&
                !typeString.equals(String.valueOf(ResourseType.VIDEO)))){
            throw new ValidationException("You have entered the wrong resource type!");
        }

        Resourse resourse = new Resourse(name, data, ResourseType.valueOf(typeString));
        resourseSource.add(resourse);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        Map<String, Object> params = new HashMap<>();
        int id = (int) (Math.random()*(600+1)) - 200;
        params.put("id", id);
        params.put("resourse_name", name);
        params.put("data", data);
        params.put("type", typeString);
        jdbcTemplate.update(INSERT_QUERY, params);
        logger.log(LoggerType.INFO, ResourseService.class, "You have added a new additional material");
        return resourse;
    }

    public Resourse getResourse(int resourseIndex) throws FileNotFoundException {
        Resourse resourse = null;
        try {
            resourse = resourseSource.get(resourseIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        int id = resourseIndex;
        Resourse resourse1 = jdbcTemplate.queryForObject(GET_QUERY, Map.of("id", id), (rs, rowNum) ->
                new Resourse(
                        rs.getString("resourse_name"),
                        rs.getString("data"),
                        (ResourseType) rs.getObject("type")
                )
        );
        return resourse;
    }

    public void deleteResourse(int resourseIndex) throws FileNotFoundException {
        try {
            resourseSource.delete(resourseIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        jdbcTemplate.update(DELETE_QUERY, Map.of("id", resourseIndex));
        logger.log(LoggerType.WARNING, ResourseService.class, "You have deleted an additional material");
    }

    public String showResources() throws FileNotFoundException {
        String resources = new String("");
        for (int i = 0; i < resourseSource.showResourses().size(); i++) {
            resources += resourseSource.get(i + 1).getName() +"\n";
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        List<Resourse> list = jdbcTemplate.query(SHOW_ALL_QUERY, (rs, rowNum) -> {
            Resourse resourse = new Resourse(
                    rs.getString("resourse_name"),
                    rs.getString("data"),
                    (ResourseType) rs.getObject("type"));
            resourse.setId(rs.getInt("id"));
            return resourse;
        });
        for (Resourse resourse : list) {
            System.out.println(resourse.getId() + " " + resourse.getName());
        }
        return resources;
    }

    public void loadResourcesToList() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        List<Resourse> list = jdbcTemplate.query(SHOW_ALL_QUERY, (rs, rowNum) -> {
            Resourse resourse = new Resourse(
                    rs.getString("resourse_name"),
                    rs.getString("data"),
                    (ResourseType) rs.getObject("type"));
            resourse.setId(rs.getInt("id"));
            resourseSource.add(resourse);
            return resourse;
        });
    }
}
