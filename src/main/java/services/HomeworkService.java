package services;

import exceptions.ValidationException;
import models.HomeWork;
import mylogger.LoggerType;
import mylogger.MyLogger;
import sources.HomeworkSource;

public class HomeworkService {
    MyLogger logger = new MyLogger();
    private final HomeworkSource homeworkSource1 = HomeworkSource.getInstance();
    private final HomeworkSource homeworkSource2 = HomeworkSource.getInstance();

    public HomeWork createHomework(String task, String deadline) {
        if (task.isBlank() || deadline.isBlank()) {
            throw new ValidationException("Validation exception!");
        }

        HomeWork homeWork = new HomeWork(task, deadline);
        homeworkSource1.add(homeWork);
        logger.log(LoggerType.INFO, ResourseService.class, "You have added a new homework!");
        return homeWork;
    }

    public void getHomework(int homeworkIndex) {
        try {
            homeworkSource1.get(homeworkIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
    }

    public void deleteHomework(int homeworkIndex) {
        try {
            homeworkSource1.delete(homeworkIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        logger.log(LoggerType.WARNING, ResourseService.class, "You have deleted a homework!");
    }

    public void showHomeworks() {homeworkSource1.showHomeworks();
    }
}
