package com.geekhub.services;

import com.geekhub.exceptions.*;
import com.geekhub.models.*;
import com.geekhub.mylogger.*;
import com.geekhub.sources.ResourseSource;

public class ResourseService {
    private final MyLogger logger;
    private final ResourseSource resourseSource1 = ResourseSource.getInstance();

    public ResourseService(MyLogger logger) {
        this.logger = logger;
    }

    /** Creates a new resource */
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
    /** Gets new resource by index from sources */
    public void getResourse(int resourseIndex) {
        try {
            resourseSource1.get(resourseIndex - 1);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
    }
    /** Deletes a resource by index */
    public void deleteResourse(int resourseIndex) {
        try {
            resourseSource1.delete(resourseIndex - 1);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        logger.log(LoggerType.WARNING, ResourseService.class, "You have deleted an additional material");
    }
    /** Shows all resources */
    public void showResources() {
        resourseSource1.showResourses().stream().map(Resourse::getName).forEach(System.out::println);
    }
}
