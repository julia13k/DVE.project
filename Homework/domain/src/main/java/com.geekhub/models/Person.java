package com.geekhub.models;

import java.io.Serializable;
import java.util.Optional;

public class Person implements Serializable {
    private String firstName;
    private String lastName;
    private String contacts;
    private String email;
    private Role role;
    private int id;
    private static final long serialVersionUID = 1L;

    public Person(String firstName, String lastName, String contacts, String email, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contacts = contacts;
        this.email = email;
        this.role = role;
    }

    public Optional<String> getFirstName() {
        return Optional.ofNullable(firstName);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Optional<String> getLastName() {
        return Optional.ofNullable(lastName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Optional<String> getContacts() { return Optional.ofNullable(contacts); }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Optional<Role> getRole() {
        return Optional.ofNullable(role);
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
