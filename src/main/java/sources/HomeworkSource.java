package sources;

import models.HomeWork;
import models.Lection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeworkSource {
    private static HomeworkSource instance;

    private List<HomeWork> homeworklist = new ArrayList<>();

    public void showHomeworks() {
        for (int i = 0; i < homeworklist.size(); i++) {
            System.out.println(homeworklist.get(i));
        }
    }

    public HomeWork get(int index) {
        return homeworklist.get(index - 1);
    }

    public void add(HomeWork newHomework){
        homeworklist.add(newHomework);
    }

    public void delete(int homeworkIndex){
        for (int i = 0; i< homeworklist.size(); i++) {
            HomeWork homeWork = homeworklist.get(i);

            if(!Objects.isNull(homeworklist.get(homeworkIndex))) {
                homeworklist.remove(homeworkIndex - 1);
                return;
            }
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
