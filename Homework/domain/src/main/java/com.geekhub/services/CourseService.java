package com.geekhub.services;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.exceptions.*;
import com.geekhub.models.*;
import com.geekhub.mylogger.*;
import com.geekhub.sources.CourseSourse;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseService {
    private final CourseSourse courseSourse;
    private final MyLogger logger;
    final String INSERT_QUERY = "INSERT INTO course (id, name) VALUES (:id, :name)";
    final String GET_QUERY = "SELECT * from course where id = :id";
    final String DELETE_QUERY = "DELETE from course where id = :id";
    final String SHOW_ALL_QUERY = "SELECT * from course";

    public CourseService(CourseSourse courseSourse, MyLogger logger) {
        this.courseSourse = courseSourse;
        this.logger = logger;
    }

    public Course createCourse(String name) throws FileNotFoundException {
        if (name.isBlank()) {
            throw new ValidationException("Validation exception");
        }
        Course course = new Course(name);
        courseSourse.add(course);
        logger.log(LoggerType.INFO, course, "You have created a new course");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        Map<String, Object> params = new HashMap<>();
        int id = (int) (Math.random()*(600+1)) - 200;
        params.put("id", id);
        params.put("name", name);
        jdbcTemplate.update(INSERT_QUERY, params);
        return course;
    }

    public Course getCourse(int courseIndex) throws FileNotFoundException {
        try {
            courseSourse.get(courseIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        int id = courseIndex;
        Course course = jdbcTemplate.queryForObject(GET_QUERY, Map.of("id", id), (rs, rowNum) ->
                new Course(
                        rs.getString("name")
                )
        );
        return course;
    }

    public void deleteCourse(int courseIndex) throws FileNotFoundException {
        try {
            courseSourse.delete(courseIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception exception");
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        jdbcTemplate.update(DELETE_QUERY, Map.of("id", courseIndex));
        logger.log(LoggerType.INFO, CourseService.class, "You have deleted a course");
    }

    public String showCourses() {
        String courses = "";
        for (int i = 0; i < courseSourse.showCourses().size(); i++) {
            courses += courseSourse.get(i + 1).getName() +"\n";
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        List<Course> list = jdbcTemplate.query(SHOW_ALL_QUERY, (rs, rowNum) -> {
            Course course = new Course(rs.getString("name"));
            course.setId(rs.getInt("id"));
            return course;
        });
        for (Course course : list) {
            System.out.println(course.getId() + " " + course.getName());
        }
        return courses;
    }

    public void loadCoursesToList() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        List<Course> list = jdbcTemplate.query(SHOW_ALL_QUERY, (rs, rowNum) -> {
            Course course = new Course(rs.getString("name"));
            course.setId(rs.getInt("id"));
            courseSourse.add(course);
            return course;
        });
    }
}
