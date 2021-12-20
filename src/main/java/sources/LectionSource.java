package sources;

import models.HomeWork;
import models.Lection;
import models.Resourse;

import java.util.*;

public class LectionSource {
    private static LectionSource instance;

    private List<Lection> lections = new ArrayList<>();

    public void showLections() {
        for (int i = 0; i < lections.size(); i++) {
            System.out.println(lections.get(i));
        }
    }

    public Lection get(int index) {
        return lections.get(index - 1);
    }

    public void add(Lection newLection){
        lections.add(newLection);
    }

    public void delete(int lectionIndex){
        for (int i = 0; i< lections.size(); i++) {
            Lection lection = lections.get(i);

            if(!Objects.isNull(lections.get(lectionIndex))) {
                lections.remove(lectionIndex - 1);
                return;
            }
        }
    }

    public static LectionSource getInstance() {
        if (instance == null){
            instance = new LectionSource();
            return instance;
        } else {
            return instance;
        }
    }
}
