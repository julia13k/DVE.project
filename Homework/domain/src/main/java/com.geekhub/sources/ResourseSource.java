package com.geekhub.sources;

import com.geekhub.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResourseSource {
    private static ResourseSource instance;
    private List<Resourse> resourses = new ArrayList<>();

    public List<Resourse> showResourses() {
        return resourses;
    }

    public Resourse get(int index) {
        return resourses.get(index - 1);
    }

    public void add(Resourse newResourse){
        resourses.add(newResourse);
    }

    public void delete(int resourseIndex){
        if (resourses.stream().anyMatch(resourse -> !Objects.isNull(resourses.get(resourseIndex)))) {
            resourses.remove(resourseIndex - 1);
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
