package com.geekhub.config;

import com.geekhub.mylogger.MyLogger;
import com.geekhub.services.*;
import com.geekhub.sources.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DatabaseConfig.class)
public class AppConfig {

    @Bean
    public CourseSourse courseSourse() {
        return new CourseSourse();
    }

    @Bean
    public CourseService courseService(CourseSourse courseSourse, MyLogger logger) {
        return new CourseService(courseSourse, logger);
    }

    @Bean
    public HomeworkSource homeworkSourse() {
        return new HomeworkSource();
    }

    @Bean
    public HomeworkService homeworkService(MyLogger logger, HomeworkSource homeworkSourse) {
        return new HomeworkService(logger, homeworkSourse);
    }

    @Bean
    public LectionSource lectionSource() {
        return new LectionSource();
    }

    @Bean
    public LectionService lectionService(LectionSource lectionSource, MyLogger logger) {
        return new LectionService(lectionSource, logger);
    }

    @Bean
    public PersonSourse personSourse() {
        return new PersonSourse();
    }

    @Bean
    public PersonService personService(PersonSourse personSourse, MyLogger logger) {
        return new PersonService(personSourse, logger);
    }

    @Bean
    public ResourseSource resourseSource() {
        return new ResourseSource();
    }

    @Bean
    public ResourseService resourseService(MyLogger logger, ResourseSource resourseSource) {
        return new ResourseService(logger, resourseSource);
    }

    @Bean
    public MyLogger logger() {
        return new MyLogger();
    }
}
