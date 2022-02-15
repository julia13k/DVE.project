package com.geekhub.services;

import com.geekhub.exceptions.*;
import com.geekhub.models.*;
import com.geekhub.mylogger.*;
import com.geekhub.sources.PersonSourse;

public class PersonService {
    private final PersonSourse personSourse = PersonSourse.getInstance();
    MyLogger logger = new MyLogger();

    public Person createPerson(String firstName, String lastName, String contacts, String email, String role) {
        if (firstName.isBlank() || lastName.isBlank() || contacts.isBlank() || email.isBlank() || role.isBlank()) {
            throw new ValidationException("You have to enter the arguments!");
        }

        if (!role.equals(String.valueOf(Role.TEACHER)) || !role.equals(String.valueOf(Role.STUDENT))) {
            throw new InvalidArgumentException("You have to enter a TEACHER or a STUDENT!");
        }
        Person person = new Person(firstName, lastName, contacts, email, Role.valueOf(role));
        personSourse.add(person);
        logger.log(LoggerType.INFO, PersonService.class, "You have added a new person");
        return person;
    }

    public Person getPerson(int personIndex) {
        try {
            personSourse.get(personIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        return personSourse.get(personIndex);
    }

    public void deletePerson(int personIndex) {
        try {
            personSourse.delete(personIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        logger.log(LoggerType.WARNING, PersonService.class, "You have deleted a person");
    }

    public String showPeople() {
        String people = new String("");
        for (int i = 0; i < personSourse.showPeople().size(); i++) {
            people += personSourse.get(i + 1).getFirstName() + " " + personSourse.get(i + 1).getLastName() +"\n";
        }
        return people;
    }
}
