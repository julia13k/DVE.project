package com.geekhub.models;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public class Lection implements Serializable {
    private String name;
    private String description;
    private ZonedDateTime creationDate;
    private List<Resourse> resources;
    private Person lecturer;
    private List<HomeWork> homeworks;
    private int id;
    private static final long serialVersionUID = 1L;

    public Lection(String name, String description) {
        this.name = name;
        this.description = description;
        this.lecturer = lecturer;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<String> getDescription() { return Optional.ofNullable(description);}

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreationDate() {return creationDate; }

    public void setCreationDate(ZonedDateTime creationDate) { this.creationDate = creationDate; }

    public List<Resourse> getResources() { return resources;}

    public void setResource(List<Resourse> resources) {
        this.resources = this.resources;
    }

    public Optional<Person> getLecturer() {
        return Optional.ofNullable(lecturer);
    }

    public void setLecturer(Person lecturer) {
        this.lecturer = lecturer;
    }

    public List<HomeWork> getHomeworks() { return homeworks; }

    public void setHomework(List<HomeWork> homeworks) {
        this.homeworks = homeworks;
    }

    public void setResources(List<Resourse> resources) { this.resources = resources; }

    public void setHomeworks(List<HomeWork> homeworks) { this.homeworks = homeworks; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
