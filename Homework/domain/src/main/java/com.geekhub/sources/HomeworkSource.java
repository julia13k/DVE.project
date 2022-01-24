package com.geekhub.sources;

import com.geekhub.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeworkSource {
    private static HomeworkSource instance;

    private List<HomeWork> homeworklist = new ArrayList<>();

    public List<HomeWork> showHomeworks() {
        return homeworklist;
    }

    public HomeWork get(int index) {
        return homeworklist.get(index - 1);
    }

    public void add(HomeWork newHomework){
        homeworklist.add(newHomework);
    }

    public void delete(int homeworkIndex){
        if (homeworklist.stream().anyMatch(homeWork -> !Objects.isNull(homeworklist.get(homeworkIndex)))) {
            homeworklist.remove(homeworkIndex - 1);
        }
    }

    public static HomeworkSource getInstance() {
        if (instance == null){
            instance = new HomeworkSource();
            return instance;
        } else {
            return instance;
        }
    }
}
