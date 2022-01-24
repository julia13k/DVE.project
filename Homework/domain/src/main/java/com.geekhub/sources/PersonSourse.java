package com.geekhub.sources;

import com.geekhub.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PersonSourse {
    private static PersonSourse instance;

    private List<Person> members = new ArrayList<>();

    public Person get(int index) {
        return members.get(index - 1);
    }

    public void add(Person newPerson) {
        members.add(newPerson);
    }

    public void delete(int personIndex){
        if (members.stream().anyMatch(person -> !Objects.isNull(members.get(personIndex)))) {
            members.remove(personIndex - 1);
        }
    }

    public List<Person> showPeople() {
        return members;
    }

    public static PersonSourse getInstance() {
        if (instance == null){
            instance = new PersonSourse();
            return instance;
        } else {
            return instance;
        }
    }
}
