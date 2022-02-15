package com.geekhub.services;

import com.geekhub.exceptions.*;
import com.geekhub.models.*;
import com.geekhub.mylogger.*;
import com.geekhub.sources.CourseSourse;

import java.util.List;

public class CourseService {
    private final CourseSourse courseSourse = CourseSourse.getInstance();
    MyLogger logger = new MyLogger();

    public Course createCourse(String name) {
        if (name.isBlank()) {
            throw new ValidationException("Validation exception");
        }
        Course course = new Course(name);
        courseSourse.add(course);
        logger.log(LoggerType.INFO, course, "You have created a new course");
        return course;
    }

    public Course getCourse(int courseIndex) {
        try {
            courseSourse.get(courseIndex - 1);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        return courseSourse.get(courseIndex - 1);
    }

    public void deleteCourse(int courseIndex) {
        try {
            courseSourse.delete(courseIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception exception");
        }
        logger.log(LoggerType.INFO, CourseService.class, "You have deleted a course");
    }

    public String showCourses() {
        StringBuilder courses = new StringBuilder(new String(""));
        for (int i = 0; i < courseSourse.showCourses().size(); i++) {
            courses.append(courseSourse.get(i + 1).getName()).append("\n");
        }
        return courses.toString();
    }
}
