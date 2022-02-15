package com.geekhub.services;

import com.geekhub.exceptions.*;
import com.geekhub.models.*;
import com.geekhub.mylogger.*;
import com.geekhub.sources.HomeworkSource;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class HomeworkService {
    MyLogger logger = new MyLogger();
    private final HomeworkSource homeworkSource = HomeworkSource.getInstance();

    public HomeWork createHomework(String task, String deadline) {
        if (task.isBlank() || deadline.isBlank()) {
            throw new ValidationException("Validation exception!");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss:SS");

        ZonedDateTime zonedDateTime = ZonedDateTime.parse(deadline, formatter);
        HomeWork homeWork = new HomeWork(task, zonedDateTime);
        homeworkSource.add(homeWork);
        logger.log(LoggerType.INFO, ResourseService.class, "You have added a new homework!");
        return homeWork;
    }

    public HomeWork getHomework(int homeworkIndex) {
        try {
            homeworkSource.get(homeworkIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        return homeworkSource.get(homeworkIndex);
    }

    public void deleteHomework(int homeworkIndex) {
        try {
            homeworkSource.delete(homeworkIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        logger.log(LoggerType.WARNING, ResourseService.class, "You have deleted a homework!");
    }

    public String showHomeworks() {
        String homeworks = new String("");
        for (int i = 0; i < homeworkSource.showHomeworks().size(); i++) {
            homeworks += homeworkSource.get(i + 1).getTask() +"\n";
        }
        return homeworks;
    }
}
