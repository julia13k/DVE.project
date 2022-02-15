package com.geekhub.services;

import com.geekhub.exceptions.*;
import com.geekhub.models.*;
import com.geekhub.mylogger.*;
import com.geekhub.sources.ResourseSource;

public class ResourseService {
    MyLogger logger = new MyLogger();
    private final ResourseSource resourseSource = ResourseSource.getInstance();

    public Resourse createResourse(String name, String data, String typeString) {
        if (name.isBlank() || data.isBlank() || typeString.isBlank()) {
            throw new ValidationException("Validation exception!");
        }

        if(!(typeString.equals(String.valueOf(ResourseType.BOOK)) &&
                !typeString.equals(String.valueOf(ResourseType.URL)) &&
                !typeString.equals(String.valueOf(ResourseType.VIDEO)))){
            throw new ValidationException("");
        }

        Resourse resourse = new Resourse(name, data, ResourseType.valueOf(typeString));
        resourseSource.add(resourse);
        logger.log(LoggerType.INFO, ResourseService.class, "You have added a new additional material");
        return resourse;
    }

    public Resourse getResourse(int resourseIndex) {
        Resourse resourse = null;
        try {
            resourse = resourseSource.get(resourseIndex - 1);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        return resourse;
    }

    public void deleteResourse(int resourseIndex) {
        try {
            resourseSource.delete(resourseIndex - 1);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        logger.log(LoggerType.WARNING, ResourseService.class, "You have deleted an additional material");
    }

    public String showResources() {
        String resources = new String("");
        for (int i = 0; i < resourseSource.showResourses().size(); i++) {
            resources += resourseSource.get(i + 1).getName() +"\n";
        }
        return resources;
    }
}
