package src.main.sources;

import src.main.models.Person;

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
        for (int i = 0; i< members.size(); i++) {
            Person person = members.get(i);

            if(!Objects.isNull(members.get(personIndex))) {
                members.remove(personIndex - 1);
                return;
            }
        }
    }

    public void showPeople() {
        for (int i = 0; i < members.size(); i++) {
            System.out.println(members.get(i));
        }
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
