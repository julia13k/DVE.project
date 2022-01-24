package com.geekhub.sources;

import com.geekhub.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseSourse {
    private static CourseSourse instance;

    private List<Course> courses = new ArrayList<>();

    public List<Course> showCourses() {
        return courses;
    }

    public Course get(int index) {
        return courses.get(index - 1);
    }

    public void add(Course newCourse){
        courses.add(newCourse);
    }

    public void delete(int courseIndex){
        for (int i = 0; i< courses.size(); i++) {
            Course lection = courses.get(i);

            if(!Objects.isNull(courses.get(courseIndex))) {
                courses.remove(courseIndex -1);
                return;
            }
        }
    }

    public static CourseSourse getInstance() {
        if (instance == null){
            instance = new CourseSourse();
            return instance;
        } else {
            return instance;
        }
    }
}
