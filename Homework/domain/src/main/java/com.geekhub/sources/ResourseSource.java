package com.geekhub.sources;

import com.geekhub.models.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ResourseSource {
    private List<Resourse> resourses = new ArrayList<>();


    public List<Resourse> showResourses() {
        return resourses;
    }

    public Resourse get(int index) {
        return resourses.get(index);
    }

    public void add(Resourse newResourse){
        resourses.add(newResourse);
    }

    public void delete(int resourseIndex){
        if (resourses.stream().anyMatch(resourse -> !Objects.isNull(resourses.get(resourseIndex)))) {
            resourses.remove(resourseIndex);
        }
    }
}
