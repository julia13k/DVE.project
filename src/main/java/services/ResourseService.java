package services;

import exceptions.InvalidArgumentException;
import exceptions.ValidationException;
import models.Resourse;
import models.ResourseType;
import mylogger.LoggerType;
import mylogger.MyLogger;
import sources.ResourseSource;

public class ResourseService {
    MyLogger logger = new MyLogger();
    private final ResourseSource resourseSource1 = ResourseSource.getInstance();
    private final ResourseSource resourseSource2 = ResourseSource.getInstance();

    public Resourse createResourse(String name, String data, String typeString) {
            if (name.isBlank() || data.isBlank() || typeString.isBlank()) {
                throw new ValidationException("Validation exception!");
            }

            if(!(typeString.equals("BOOK") && !typeString.equals("URL") && !typeString.equals("VIDEO"))){
                throw new ValidationException("");
            }

            Resourse resourse = new Resourse(name, data, ResourseType.valueOf(typeString));
            resourseSource1.add(resourse);
        logger.log(LoggerType.INFO, ResourseService.class, "You have added a new additional material");
        return resourse;
    }

    public void getResourse(int resourseIndex) {
        try {
            resourseSource1.get(resourseIndex - 1);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
    }

    public void deleteResourse(int resourseIndex) {
        try {
            resourseSource1.delete(resourseIndex - 1);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        logger.log(LoggerType.WARNING, ResourseService.class, "You have deleted an additional material");
    }

    public void showResources() {
        resourseSource1.showResourses();
    }
}
