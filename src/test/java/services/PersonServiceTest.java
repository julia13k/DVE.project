package services;

import exceptions.InvalidArgumentException;
import exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

    private PersonService personService;

    @BeforeEach
    void setUp() {
        personService = new PersonService();
    }

    @Test
    void failed_to_create_person_because_of_wrong_type() {
        assertThrows(
            InvalidArgumentException.class,
            () -> personService.createPerson("f", "l", "c", "e", "role")
        );
    }

    @Test
    void failed_to_create_person_because_of_empty_arguments() {
        assertThrows(
            ValidationException.class,
            () -> personService.createPerson("", "", "", "", "")
        );
    }

    @Test
    void failed_to_create_person_because_of_null_arguments() {
        assertThrows(
            NullPointerException.class,
            () -> personService.createPerson(null, null, null, null, null)
        );
    }

    @Test
    void does_not_failed_to_get_person_because_of_index_out_of_bounds() {
        assertDoesNotThrow(
            () -> personService.getPerson(98)
        );
    }

    @Test
    void does_not_failed_to_delete_person_because_of_index_out_of_bounds() {
        assertDoesNotThrow(
            () -> personService.deletePerson(98)
        );
    }
}