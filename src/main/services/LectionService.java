package src.main.services;

import src.main.exceptions.InvalidArgumentException;
import src.main.exceptions.LessonNotFoundException;
import src.main.exceptions.ValidationException;
import models.*;
import src.main.mylogger.LoggerType;
import src.main.mylogger.MyLogger;
import src.main.sources.HomeworkSource;
import src.main.sources.LectionSource;
import src.main.sources.PersonSourse;
import src.main.sources.ResourseSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectionService {
    MyLogger logger = new MyLogger();

    private Map<Lection, List<HomeWork>> homeworkByLectureList = new HashMap<>();
    private Map<Lection, List<Resourse>> resourceByLectureList = new HashMap<>();

    private List<HomeWork> homeWorkList = new ArrayList<>();
    private List<Resourse> resourseList = new ArrayList<>();

    private final LectionSource lectionSource = LectionSource.getInstance();
    private final LectionSource lectionSource2 = LectionSource.getInstance();
    private final PersonSourse personSourse = PersonSourse.getInstance();
    private final ResourseSource resourseSource = ResourseSource.getInstance();
    private final HomeworkSource homeworkSource = HomeworkSource.getInstance();


    public Lection createLection(String name, String description) {
            if (name.isBlank() || description.isBlank()) {
                throw new ValidationException("You have to type the arguments of a lection!");
            }
            Lection lection = new Lection(name, description);
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

    public void addResource(int lectureIndex, int resourseIndex) {
            Lection lection = lectionSource.get(lectureIndex);
            lection.setResource(resourseSource.get(resourseIndex));
            resourseList.add(lection.getResource());
            resourceByLectureList.put(lectionSource.get(lectureIndex), resourseList);

    }

    public void addHomework(int lectureIndex, int homeworkIndex) {
        Lection lection = lectionSource.get(lectureIndex);
        lection.setHomework(homeworkSource.get(homeworkIndex));
        homeWorkList.add(lection.getHomework());
        homeworkByLectureList.put(lectionSource.get(lectureIndex), homeWorkList);
    }

    public void showResourcesByLection() {
        for(Map.Entry<Lection, List<Resourse>> map : resourceByLectureList.entrySet()) {
            System.out.printf("%s - %s \n", map.getKey().getName(), map.getValue());
        }
    }

    public void showHomeworksByLection() {
        for(Map.Entry<Lection, List<HomeWork>> map : homeworkByLectureList.entrySet()) {
            System.out.printf("%s - %s \n", map.getKey().getName(), map.getValue());
        }
    }
}
