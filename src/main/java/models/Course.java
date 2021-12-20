package models;

public class Course {
    private String name;
    private Lection[] lections;
    private Person[] students;

    public Course(String name) {
        this.name = name;
    }
    public Course(String name, Lection[] lections, Person[] students) {
        this.name = name;
        this.lections = lections;
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Lection[] getLections() {
        return lections;
    }

    public void setLections(Lection[] lections) {
        this.lections = lections;
    }

    public Person[] getStudents() {
        return students;
    }

    public void setStudents(Person[] students) {
        this.students = students;
    }
}
