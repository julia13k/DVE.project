package com.geekhub.services;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.exceptions.*;
import com.geekhub.models.*;
import com.geekhub.mylogger.*;
import com.geekhub.sources.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class LectionService{

    final String INSERT_QUERY = "INSERT INTO lecture (id, title, description, creationdate) " +
            "VALUES (:id, :title, :description, :creationdate)";
    final String GET_QUERY = "SELECT * from lecture where id = :id";
    final String DELETE_QUERY = "DELETE from lecture where id = :id";
    final String SHOW_ALL_QUERY = "SELECT * from lecture";
    final String ADD_RESOURSE_QUERY = "SELECT * from lecture RIGHT JOIN resourse ON lecture.id=:id AND resourse.id=:id";
    final String ADD_HOMEWORK_QUERY = "SELECT * from lecture RIGHT JOIN homework ON lecture.id=:id AND homework.id=:id";
    final String ADD_TEACHER_QUERY = "SELECT * from lecture RIGHT JOIN person ON lecture.id=:id AND person.id=:id";

    private final LectionSource lectionSource;
    private final MyLogger logger;

    public LectionService(LectionSource lectionSource, MyLogger logger) {
        this.lectionSource = lectionSource;
        this.logger = logger;
    }

    public Lection createLection(String name, String description) throws FileNotFoundException {
        if (name.isBlank() || description.isBlank()) {
            throw new ValidationException("You have to type the arguments of a lection!");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss:SS");
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC+2"));
        ZonedDateTime zonedDateTime = ZonedDateTime.of(now, ZoneId.of("UTC+2"));
        Lection lection = new Lection(name, description);
        lection.setCreationDate(zonedDateTime);
        lection.setResource(new ArrayList<>());
        lectionSource.add(lection);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        Map<String, Object> params = new HashMap<>();
        int id = (int) (Math.random()*(600+1)) - 200;
        params.put("id", id);
        params.put("title", name);
        params.put("description", description);
        params.put("creationdate", zonedDateTime);
        jdbcTemplate.update(INSERT_QUERY, params);
        logger.log(LoggerType.INFO, lection, "You have created a new lection");
        return lection;
    }

    public Lection getLection(int lectionIndex) throws FileNotFoundException {
        try {
            if (lectionSource.get(lectionIndex) == null) {
                throw new LessonNotFoundException("Lesson");
            }
            lectionSource.get(lectionIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        } catch (LessonNotFoundException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Lesson is not found");
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        int id = lectionIndex;
        Lection lection = jdbcTemplate.queryForObject(GET_QUERY, Map.of("id", id), (rs, rowNum) ->
                new Lection(
                        rs.getString("title"),
                        rs.getString("description")
                )
        );
        return lectionSource.get(lectionIndex);
    }

    public void deleteLection(int lectionIndex) throws FileNotFoundException {
        try {
            lectionSource.delete(lectionIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        jdbcTemplate.update(DELETE_QUERY, Map.of("id", lectionIndex));
        logger.log(LoggerType.WARNING, LectionService.class, "You have deleted a lection");
    }

    public String showLections() throws FileNotFoundException {
        String lections = new String("");
        for (int i = 0; i < lectionSource.showLections().size(); i++) {
            lections += lectionSource.get(i + 1).getName() +"\n";
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        List<Lection> list = jdbcTemplate.query(SHOW_ALL_QUERY, (rs, rowNum) -> {
            Lection lection = new Lection(
                    rs.getString("title"),
                    rs.getString("description"));
            lection.setId(rs.getInt("id"));
            return lection;
        });
        for (Lection lection : list) {
            System.out.println(lection.getId() + " " + lection.getName());
        }
        return lections;
    }

    public void addTeacher(int lectureIndex, int teacherIndex) {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var personSourse = applicationContext.getBean(PersonSourse.class);
        Person person = personSourse.get(teacherIndex);
        if (!person.getRole().equals(Role.TEACHER)) {
            throw new InvalidArgumentException("You have to choose a teacher!");
        }
        Lection lection = lectionSource.get(lectureIndex);
        lection.setLecturer(person);
        lectionSource.get(lectureIndex).setLecturer(personSourse.get(teacherIndex));

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        jdbcTemplate.update(ADD_TEACHER_QUERY, Map.of("lecture.id", lectureIndex, "person.id", teacherIndex));
    }

    public void addResource(int lectureIndex, int resourceIndex) throws FileNotFoundException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var resourseSource = applicationContext.getBean(ResourseSource.class);
        lectionSource.get(lectureIndex).getResources().add(resourseSource.get(resourceIndex));

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        jdbcTemplate.update(ADD_RESOURSE_QUERY, Map.of("lecture.id", lectureIndex, "resourse.id", resourceIndex));
    }

    public void addHomework(int lectureIndex, int homeworkIndex) throws FileNotFoundException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class);
        applicationContext.refresh();
        var homeworkSource = applicationContext.getBean(HomeworkSource.class);
        lectionSource.get(lectureIndex).getHomeworks().add(homeworkSource.get(homeworkIndex));
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        jdbcTemplate.update(ADD_HOMEWORK_QUERY, Map.of("lecture.id", lectureIndex, "homework.id", homeworkIndex));
    }

    public Map<Optional<String>, List<HomeWork>> groupByHomework() {
        return lectionSource.groupByHomework();
    }

    public Map<Optional<String>, List<Resourse>> groupByResource() {
        return lectionSource.groupByResources();
    }

    public void loadLecturesToList() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        List<Lection> list = jdbcTemplate.query(SHOW_ALL_QUERY, (rs, rowNum) -> {
            Lection lection = new Lection(
                    rs.getString("title"),
                    rs.getString("description")
            );
            lection.setId(rs.getInt("id"));
            lectionSource.add(lection);
            return lection;
        });
    }
}
