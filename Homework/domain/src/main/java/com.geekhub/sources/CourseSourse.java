package com.geekhub.sources;

import com.geekhub.models.*;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CourseSourse {
    private List<Course> courses = new ArrayList<>();

    public List<Course> showCourses() {
        return courses;
    }

    public Course get(int index) {
        return courses.get(index);
    }

    public void add(Course newCourse) {
        courses.add(newCourse);
    }

    public void delete(int courseIndex) {
        if(!Objects.isNull(courses.get(courseIndex))) {
            courses.remove(courseIndex);
            return;
        }
    }
}
