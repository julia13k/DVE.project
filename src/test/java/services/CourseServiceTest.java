package services;

import exceptions.ValidationException;
import models.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseServiceTest {

    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseService = new CourseService();
    }

    @Test
    void failed_to_create_course_because_of_empty_title() {
        assertThrows(
            ValidationException.class,
            () -> courseService.createCourse("")
        );
    }

    @Test
    void failed_to_create_course_because_of_null_title() {
        assertThrows(
            NullPointerException.class,
            () -> courseService.createCourse(null)
        );
    }

    @Test
    void does_not_failed_to_get_course_because_of_index_out_of_bounds() {
        assertDoesNotThrow(
            () -> courseService.getCourse(98)
        );
    }

    @Test
    void does_not_failed_to_delete_course_because_of_index_out_of_bounds() {
        assertDoesNotThrow(
            () -> courseService.deleteCourse(98)
        );
    }

}