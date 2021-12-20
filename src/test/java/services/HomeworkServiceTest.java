package services;

import exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HomeworkServiceTest {
    private HomeworkService homeworkService;

    @BeforeEach
    void setUp() {
        homeworkService = new HomeworkService();
    }


    @Test
    void failed_to_create_homework_because_of_empty_arguments() {
        assertThrows(
            ValidationException.class,
            () -> homeworkService.createHomework("", "")
        );
    }

    @Test
    void failed_to_create_homework_because_of_null_arguments() {
        assertThrows(
            NullPointerException.class,
            () -> homeworkService.createHomework(null, null)
        );
    }

    @Test
    void does_not_failed_to_get_homework_because_of_index_out_of_bounds() {
        assertDoesNotThrow(
            () -> homeworkService.getHomework(98)
        );
    }

    @Test
    void does_not_failed_to_delete_homework_because_of_index_out_of_bounds() {
        assertDoesNotThrow(
            () -> homeworkService.deleteHomework(98)
        );
    }

}