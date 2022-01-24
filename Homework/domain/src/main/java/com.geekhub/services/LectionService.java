package com.geekhub.services;

import com.geekhub.exceptions.*;
import com.geekhub.models.*;
import com.geekhub.mylogger.*;
import com.geekhub.sources.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LectionService{
    MyLogger logger = new MyLogger();

    private Map<Lection, List<HomeWork>> homeworkByLectureList = new HashMap<>();
    private Map<Lection, List<Resourse>> resourceByLectureList = new HashMap<>();

    private List<HomeWork> homeWorkList = new ArrayList<>();
    private List<Resourse> resourseList = new ArrayList<>();

    private final LectionSource lectionSource = LectionSource.getInstance();
    private final PersonSourse personSourse = PersonSourse.getInstance();
    private final ResourseSource resourseSource = ResourseSource.getInstance();
    private final HomeworkSource homeworkSource = HomeworkSource.getInstance();


    public Lection createLection(String name, String description) {
            if (name.isBlank() || description.isBlank()) {
                throw new ValidationException("You have to type the arguments of a lection!");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss:SS");
            LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC+2"));
            ZonedDateTime zonedDateTime = ZonedDateTime.of(now, ZoneId.of("UTC+2"));
            Lection lection = new Lection(name, description);
            lection.setCreationDate(zonedDateTime);
            lection.setResource(new ArrayList<>());
            lectionSource.add(lection);
            logger.log(LoggerType.INFO, lection, "You have created a new lection");
            return lection;
    }

    public void getLection(int lectionIndex) {
        try {
            if (lectionSource.get(lectionIndex) == null);
            {
                throw new LessonNotFoundException("Lesson");
            }
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        } catch (LessonNotFoundException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Lesson is not found");
        }
    }

    public void deleteLection(int lectionIndex) {
        try {
            lectionSource.delete(lectionIndex);
        } catch (IndexOutOfBoundsException e) {
            logger.log(LoggerType.ERROR, e.getClass(), "Index out of bounds exception");
        }
        logger.log(LoggerType.WARNING, LectionService.class, "You have deleted a lection");
    }

    public void showLections() {
        lectionSource.showLections();
    }

    public void addTeacher(int lectureIndex, int teacherIndex) {
        Person person = personSourse.get(teacherIndex);
        if (!person.getRole().equals(Role.TEACHER)) {
            throw new InvalidArgumentException("You have to choose a teacher!");
        }
        Lection lection = lectionSource.get(lectureIndex);
        lection.setLecturer(person);
    }

    public void addResource(int lectureIndex, int resourceIndex) {
        lectionSource.get(lectureIndex).getResources().add(resourseSource.get(resourceIndex));
    }

    public void addHomework(int lectureIndex, int homeworkIndex) {
        lectionSource.get(lectureIndex).getHomeworks().add(homeworkSource.get(homeworkIndex));
    }

    public Map<Optional<String>, List<HomeWork>> groupByHomework() {
        return lectionSource.groupByHomework();
    }

    public Map<Optional<String>, List<Resourse>> groupByResource() {
        return lectionSource.groupByResources();
    }

//    public Map<Lection, List<Resourse>> addResource(int lectureIndex, List<Resourse> resourseList) {
//        return (Map<Lection, List<Resourse>>) resourceByLectureList.put(lectionSource.get(lectureIndex), resourseList);
//    }
//
//    public Map<Lection, List<HomeWork>> addHomework(int lectureIndex, List<HomeWork> homeWorkList) {
//        return (Map<Lection, List<HomeWork>>) homeworkByLectureList.put(lectionSource.get(lectureIndex), homeWorkList);
//    }
}
