package services;

import exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourseServiceTest {
    private ResourseService resourseService;

    @BeforeEach
    void setUp() {
        resourseService = new ResourseService();
    }

    @Test
    void failed_to_create_resourse_because_of_wrong_type() {
        assertThrows(
            ValidationException.class,
            () -> resourseService.createResourse("n", "d", "role")
        );
    }

    @Test
    void failed_to_create_resourse_because_of_empty_arguments() {
        assertThrows(
            ValidationException.class,
            () -> resourseService.createResourse("", "","")
        );
    }

    @Test
    void failed_to_create_resourse_because_of_null_arguments() {
        assertThrows(
            NullPointerException.class,
            () -> resourseService.createResourse(null, null, null)
        );
    }

    @Test
    void does_not_failed_to_get_resourse_because_of_index_out_of_bounds() {
        assertDoesNotThrow(
            () -> resourseService.getResourse(98)
        );
    }

    @Test
    void does_not_failed_to_delete_resourse_because_of_index_out_of_bounds() {
        assertDoesNotThrow(
            () -> resourseService.deleteResourse(98)
        );
    }

}