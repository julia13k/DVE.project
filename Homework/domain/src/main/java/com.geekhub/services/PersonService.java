package com.geekhub.services;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.exceptions.*;
import com.geekhub.models.*;
import com.geekhub.mylogger.*;
import com.geekhub.sources.PersonSourse;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonService {
    private final PersonSourse personSourse;
    private final MyLogger logger;
    final String INSERT_QUERY = "INSERT INTO person (id, firstName, lastName, contacts, email, role) " +
            "VALUES (:id, :firstName, :lastName, :contacts, :email, :role)";
    final String GET_QUERY = "SELECT * from person where id = :id";
    final String DELETE_QUERY = "DELETE from person where id = :id";
    final String SHOW_ALL_QUERY = "SELECT * from person";

    public PersonService(PersonSourse personSourse, MyLogger logger) {
        this.personSourse = personSourse;
        this.logger = logger;
    }

    public Person createPerson(String firstName, String lastName, String contacts, String email, String role) throws FileNotFoundException {
        if (firstName.isBlank() || lastName.isBlank() || contacts.isBlank() || email.isBlank() || role.isBlank()) {
            throw new ValidationException("You have to enter the arguments!");
        }

        if (!role.equals(String.valueOf(Role.TEACHER)) || !role.equals(String.valueOf(Role.STUDENT))) {
            throw new InvalidArgumentException("You have to enter a TEACHER or a STUDENT!");
        }
        Person person = new Person(firstName, lastName, contacts, email, Role.valueOf(role));
        personSourse.add(person);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        Map<String, Object> params = new HashMap<>();
        int id = (int) (Math.random()*(600+1)) - 200;
        params.put("id", id);
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("contacts", contacts);
        params.put("email", email);
        params.put("role", role);
        jdbcTemplate.update(INSERT_QUERY, params);
        logger.log(LoggerType.INFO, PersonService.class, "You have added a new person");
        return person;
    }

    public Person getPerson(int personIndex) throws FileNotFoundException {
        try {
            personSourse.get(personIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        int id = personIndex;
        return jdbcTemplate.queryForObject(GET_QUERY, Map.of("id", id), (rs, rowNum) ->
                new Person(
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("contacts"),
                        rs.getString("email"),
                        Role.valueOf(rs.getString("role"))));
    }

    public void deletePerson(int personIndex) throws FileNotFoundException {
        try {
            personSourse.delete(personIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        jdbcTemplate.update(DELETE_QUERY, Map.of("id", personIndex));
        logger.log(LoggerType.WARNING, PersonService.class, "You have deleted a person");
    }

    public String showPeople() {
        String people = new String("");
        for (int i = 0; i < personSourse.showPeople().size(); i++) {
            people += personSourse.get(i + 1).getFirstName() + " " + personSourse.get(i + 1).getLastName() +"\n";
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        List<Person> list = jdbcTemplate.query(SHOW_ALL_QUERY, (rs, rowNum) -> {
            Person person = new Person(
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("contacts"),
                    rs.getString("email"),
                    Role.valueOf(rs.getString("role")));
            person.setId(rs.getInt("id"));
            return person;
        });
        for (Person person : list) {
            System.out.println(person.getId() + " " + person.getFirstName() + " " + person.getLastName());
        }
        return people;
    }

    public void loadPeopleToList() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                DatabaseConfig.class, AppConfig.class);
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("jdbcTemplate");
        List<Person> list = jdbcTemplate.query(SHOW_ALL_QUERY, (rs, rowNum) -> {
            Person person = new Person(
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("contacts"),
                    rs.getString("email"),
                    Role.valueOf(rs.getString("role")));
            person.setId(rs.getInt("id"));
            personSourse.add(person);
            return person;
        });
    }
}
