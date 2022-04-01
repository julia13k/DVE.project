package api.console;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileNotFoundException;

public class GeneralMenu {
    ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.geekhub.properties");
    private final ArgumentsScanner scanner;
    private final CourseMenu courseMenu;
    private final LectionMenu lectionMenu;
    private final PersonMenu personMenu;
    private final ResourseMenu resourseMenu;
    private final HomeworkMenu homeworkMenu;
    private final MyLoggerMenu myLoggerMenu;
    int option;

    public GeneralMenu(ArgumentsScanner scanner, CourseMenu courseMenu, LectionMenu lectionMenu, PersonMenu personMenu,
                       ResourseMenu resourseMenu, HomeworkMenu homeworkMenu, MyLoggerMenu myLoggerMenu) {
        this.scanner = scanner;
        this.courseMenu = courseMenu;
        this.lectionMenu = lectionMenu;
        this.personMenu = personMenu;
        this.resourseMenu = resourseMenu;
        this.homeworkMenu = homeworkMenu;
        this.myLoggerMenu = myLoggerMenu;
    }

    void startGeneralMenu() throws FileNotFoundException {
        printMenu();
        startWork();
    }

    private void printMenu() {
        System.out.print(new StringBuilder("Welcome to application menu,")
                .append("please, press ENTER to continue"));
        scanner.getLine();

        System.out.println(new StringBuilder
                ("\n1 - Course menu\n")
                .append("2 - Lections menu\n")
                .append("3 - Person menu\n")
                .append("4 - Additional material menu\n")
                .append("5 - Homework menu\n")
                .append("6 - Logger menu\n")
                .append("7 - Exit\n"));
    }

    private void startWork() throws FileNotFoundException {
        System.out.print(new StringBuilder("You are using general menu\n")
                .append("Please enter the number you have chosen:\n"));
        option = scanner.getNumber();
        while (!(option == 7)) {
            switch (option) {
                case 1:
                    courseMenu.startCourseMenu();
                    printMenu();
                    startWork();
                    break;
                case 2:
                    lectionMenu.startLectionMenu();
                    printMenu();
                    startWork();
                    break;
                case 3:
                    personMenu.startPersonMenu();
                    printMenu();
                    startWork();
                    break;
                case 4:
                    resourseMenu.startResourseMenu();
                    printMenu();
                    startWork();
                    break;
                case 5:
                    homeworkMenu.startHomeworkMenu();
                    printMenu();
                    startWork();
                case 6:
                    myLoggerMenu.startLoggerMenu();
                    printMenu();
                    startWork();
                default:
                    System.out.println("Wrong menu number! Choose between 1-6.");
                    startWork();
                    break;
            }
            scanner.getScanner().close();
        }
    }


    public void getStorageTypeFromUser() {
        String storageType = "";
        System.out.println(new StringBuilder
                ("Please enter the number of a storage that you have chosen for logs:\n")
                .append("1 - In file\n")
                .append("2 - In memory\n")
                .append("3 - Both in file and in memory\n"));
        int option = scanner.getNumber();
        switch (option) {
            case 1:
                storageType += "file";
                break;
            case 2:
                storageType += "memory";
                break;
            case 3:
                storageType += "both";
                break;
            default:
                System.out.println("Wrong input! Choose a number between 1-3");
                getStorageTypeFromUser();
                break;
        }
        myLoggerMenu.getLogger().setStorageType(storageType);
    }
}
