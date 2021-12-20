package sources;


import models.Lection;
import models.Resourse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResourseSource {
    private static ResourseSource instance;
    private List<Resourse> resourses = new ArrayList<>();

    public void showResourses() {
        for (int i = 0; i < resourses.size(); i++) {
            System.out.println(resourses.get(i));
        }
    }

    public Resourse get(int index) {
        return resourses.get(index - 1);
    }

    public void add(Resourse newResourse){
        resourses.add(newResourse);
    }

    public void delete(int resourseIndex){
        for (int i = 0; i < resourses.size(); i++) {
            Resourse resourse = resourses.get(i);

            if(!Objects.isNull(resourses.get(resourseIndex))) {
                resourses.remove(resourseIndex - 1);
                return;
            }
        }
    }

    public static ResourseSource getInstance() {
        if (instance == null){
            instance = new ResourseSource();
            return instance;
        } else {
            return instance;
        }
    }
}
