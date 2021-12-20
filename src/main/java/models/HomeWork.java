package models;

import java.util.Date;

public class HomeWork {
    private String task;
    private String deadline;

    public HomeWork(String task, String deadline) {
        this.task = task;
        this.deadline = deadline;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
