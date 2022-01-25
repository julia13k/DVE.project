package com.geekhub.services;

import com.geekhub.exceptions.*;
import com.geekhub.models.*;
import com.geekhub.mylogger.*;
import com.geekhub.sources.CourseSourse;

public class CourseService {
    private final CourseSourse courseSourse = CourseSourse.getInstance();
    MyLogger logger = new MyLogger();

    /** Creates a new course */
    public Course createCourse(String name) {
        if (name.isBlank()) {
            throw new ValidationException("Validation exception");
        }
            Course course = new Course(name);
            courseSourse.add(course);
            logger.log(LoggerType.INFO, course, "You have created a new course");
            return course;
    }
    /** Gets new course by index from sources  */
    public void getCourse(int courseIndex) {
        try {
            courseSourse.get(courseIndex - 1);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
    }
    /** Deletes a course by index */
    public void deleteCourse(int courseIndex) {
        try {
            courseSourse.delete(courseIndex - 1);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception exception");
        }
        logger.log(LoggerType.INFO, CourseService.class, "You have deleted a course");
    }
    /** Shows all courses */
    public void showCourses() {
        courseSourse.showCourses().stream().map(Course::getName).forEach(System.out::println);
    }
}
