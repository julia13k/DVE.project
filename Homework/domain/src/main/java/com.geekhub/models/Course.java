package com.geekhub.models;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class Course implements Serializable {
    private String name;
    private List<Lection> lections;
    private List<Person> students;
    private static final long serialVersionUID = 1L;

    public Course(String name) {
        this.name = name;
    }
    public Course(String name, List<Lection> lections, List<Person> students) {
        this.name = name;
        this.lections = lections;
        this.students = students;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Lection> getLections() {return lections; }

    public void setLections(List<Lection> lections) {
        this.lections = lections;
    }

    public List<Person> getStudents() {return students; }

    public void setStudents(List<Person> students) {
        this.students = students;
    }
}
