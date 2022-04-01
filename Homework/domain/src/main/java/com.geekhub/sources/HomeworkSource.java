package com.geekhub.sources;

import com.geekhub.models.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class HomeworkSource {
    private List<HomeWork> homeworklist = new ArrayList<>();


    public List<HomeWork> showHomeworks() {
        return homeworklist;
    }

    public HomeWork get(int index) {
        return homeworklist.get(index);
    }

    public void add(HomeWork newHomework){
        homeworklist.add(newHomework);
    }

    public void delete(int homeworkIndex){
        if (homeworklist.stream().anyMatch(homeWork -> !Objects.isNull(homeworklist.get(homeworkIndex)))) {
            homeworklist.remove(homeworkIndex);
        }
    }
}
