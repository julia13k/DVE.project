package services;

import exceptions.InvalidArgumentException;
import exceptions.ValidationException;
import models.Person;
import models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LectionServiceTest {

    private LectionService lectionService;
    private PersonService personService;
    private ResourseService resourseService;

    @BeforeEach
    void setUp() {
        lectionService = new LectionService();
        personService = new PersonService();
        resourseService = new ResourseService();
    }


    @Test
    void failed_to_create_lection_because_of_student_person_type() {
        personService.createPerson("f", "l", "c", "e", "STUDENT");
        assertThrows(
            ValidationException.class,
            () -> lectionService.createLection("", "")
        );
    }

    @Test
    void failed_to_create_lection_because_of_empty_arguments() {
        personService.createPerson("f", "l", "c", "e", "TEACHER");
        assertThrows(
            ValidationException.class,
            () -> lectionService.createLection("", "")
        );
    }

    @Test
    void failed_to_create_lection_because_of_null_arguments() {
        personService.createPerson("f", "l", "c", "e", "TEACHER");
        assertThrows(
            NullPointerException.class,
            () -> lectionService.createLection(null, null)
        );
    }

    @Test
    void does_not_failed_to_get_lection_because_of_index_out_of_bounds() {
        assertDoesNotThrow(
            () -> lectionService.getLection(98)
        );
    }

    @Test
    void does_not_failed_to_delete_lection_because_of_index_out_of_bounds() {
        assertDoesNotThrow(
            () -> lectionService.deleteLection(98)
        );
    }

    @Test
    void failed_to_add_resourse_because_of_wrong_lection_index() {
        resourseService.createResourse("name", "data", "BOOK");
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> lectionService.addResource(98, 0)
        );
    }

    @Test
    void failed_to_add_resourse_because_of_wrong_resourse_index() {
        personService.createPerson("f", "l", "c", "e", "TEACHER");
        lectionService.createLection("name", "description");
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> lectionService.addResource(1, 1)
        );
    }
}