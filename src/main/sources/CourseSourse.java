package src.main.sources;

import src.main.models.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseSourse {
    private static CourseSourse instance;

    private List<Course> courses = new ArrayList<>();

    public void showCourses() {
        for (int i = 0; i < courses.size(); i++) {
            System.out.println(courses.get(i));
        }
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
