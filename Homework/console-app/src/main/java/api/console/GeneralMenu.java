package api.console;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GeneralMenu {

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

    public ArgumentsScanner getScanner() {
        return scanner;
    }

    public CourseMenu getCourseMenu() {
        return courseMenu;
    }

    public LectionMenu getLectionMenu() {
        return lectionMenu;
    }

    public PersonMenu getPersonMenu() {
        return personMenu;
    }

    public ResourseMenu getResourseMenu() {
        return resourseMenu;
    }

    public HomeworkMenu getHomeworkMenu() {
        return homeworkMenu;
    }

    public MyLoggerMenu getMyLoggerMenu() {
        return myLoggerMenu;
    }

    public int getOption() {
        return option;
    }

    /** Main method for implementing application menu */
    void startGeneralMenu() {
        printMenu();
        startWork();
    }
    /** Prints all options of an application menu */
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
    /** The implementation of an application menu options according to arguments received from scanner*/
    private void startWork() {
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
        }
    }

    public void setStorageType(String value) {
        FileInputStream fileInputStream;
        Properties property = new Properties();
        try {
            fileInputStream = new FileInputStream("Homework/domain/src/main/resources/application.properties");
            property.load(fileInputStream);
            property.setProperty("logger.storage.type", value);
        } catch (FileNotFoundException ex) {
            System.err.println("There is no file!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void getStorageTypeFromUser() {
        System.out.println(new StringBuilder
                ("Please enter the number of a storage that you have chosen for logs:\n")
                .append("1 - In file\n")
                .append("2 - In memory\n")
                .append("3 - Both in file and in memory\n"));
        int option = scanner.getNumber();
        switch (option) {
            case 1:
                setStorageType("file");
                break;
            case 2:
                setStorageType("memory");
                break;
            case 3:
                setStorageType("both");
                break;
            default:
                System.out.println("Wrong input! Choose a number between 1-3");
                getStorageTypeFromUser();
                break;
        }
    }
}
