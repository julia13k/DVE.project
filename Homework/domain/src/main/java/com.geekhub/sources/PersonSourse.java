package com.geekhub.sources;

import com.geekhub.models.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class PersonSourse {
    private List<Person> members = new ArrayList<>();


    public Person get(int index) {
        return members.get(index);
    }

    public void add(Person newPerson) {
        members.add(newPerson);
    }

    public void delete(int personIndex){
        if (members.stream().anyMatch(person -> !Objects.isNull(members.get(personIndex)))) {
            members.remove(personIndex);
        }
    }

    public List<Person> showPeople() {
        return members;
    }
}
