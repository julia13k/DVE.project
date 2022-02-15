package com.geekhub.sources;

import com.geekhub.models.HomeWork;
import com.geekhub.models.Lection;
import com.geekhub.models.Resourse;

import java.util.*;
import java.util.stream.Collectors;

public class LectionSource {
    private static LectionSource instance;

    private List<Lection> lections = new ArrayList<>();

    public Map<Optional<String>, List<HomeWork>> groupByHomework() {
        return lections.stream().collect(Collectors.toMap(Lection::getName, Lection::getHomeworks));
    }

    public Map<Optional<String>, List<Resourse>> groupByResources() {
        return lections.stream().collect(Collectors.toMap(Lection::getName, Lection::getResources));
    }

    public List<Lection> sortByDate() {
        return lections.stream().sorted(Comparator.comparing(Lection::getCreationDate)).collect(Collectors.toList());
    }

    public List<Lection> showLections() {
        return lections;
    }

    public Lection get(int index) {
        int actualIndex = index - 1;
        if (lections.size() >= actualIndex) {
            return lections.get(actualIndex);
        }

        throw new IllegalArgumentException("Invalid params");
    }

    public void add(Lection newLection) {
        lections.add(newLection);
    }

    public void delete(int lectionIndex) {
        for (int i = 0; i < lections.size(); i++) {
            Lection lection = lections.get(i);

            if (Objects.nonNull(lections.get(lectionIndex))) {
                lections.remove(lectionIndex - 1);
            }
        }
    }

    public static LectionSource getInstance() {
        if (instance == null) {
            instance = new LectionSource();
            return instance;
        } else {
            return instance;
        }
    }
}
