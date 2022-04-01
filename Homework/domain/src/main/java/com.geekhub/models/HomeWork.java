package com.geekhub.models;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Optional;

public class HomeWork implements Serializable {
    private String task;
    private ZonedDateTime deadline;
    private int id;
    private static final long serialVersionUID = 1L;

    public HomeWork(String task, ZonedDateTime deadline) {
        this.task = task;
        this.deadline = deadline;
    }

    public Optional<String> getTask() {
        return Optional.ofNullable(task);
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Optional<ZonedDateTime> getDeadline() {
        return Optional.ofNullable(deadline);
    }

    public void setDeadline(ZonedDateTime deadline) {
        this.deadline = deadline;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
