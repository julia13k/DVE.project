package com.geekhub.sources;

import com.geekhub.models.HomeWork;
import com.geekhub.models.Lection;
import com.geekhub.models.Resourse;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class LectionSource {
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
        if (lections.size() >= index) {
            return lections.get(index);
        }

        throw new IllegalArgumentException("Invalid params");
    }

    public void add(Lection newLection) {
        lections.add(newLection);
    }

    public void delete(int lectionIndex) {
        if (Objects.nonNull(lections.get(lectionIndex))) {
            lections.remove(lectionIndex);
            return;
        }
    }
}
