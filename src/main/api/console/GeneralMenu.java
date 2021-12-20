package src.main.api.console;

import services.*;
import src.main.services.CourseService;

public class GeneralMenu {

    ArgumentsScanner scanner = new ArgumentsScanner();
    CourseService courseService = new CourseService();
    CourseMenu courseMenu = new CourseMenu(courseService);
    LectionService lectionService = new LectionService();
    LectionMenu lectionMenu = new LectionMenu(lectionService);
    PersonService personService = new PersonService();
    PersonMenu personMenu = new PersonMenu(personService);
    ResourseService resourseService = new ResourseService();
    ResourseMenu resourseMenu = new ResourseMenu(resourseService);
    HomeworkService homeworkService = new HomeworkService();
    HomeworkMenu homeworkMenu = new HomeworkMenu(homeworkService);
    int option;

    void startGeneralMenu() {
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
            .append("6 - Exit\n"));
    }

    private void startWork() {
        System.out.print(new StringBuilder("You are using general menu\n")
            .append("Please enter the number you have chosen:\n"));
        option = scanner.getNumber();
        while (!(option == 6)) {
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
                default:
                    System.out.println("Wrong menu number! Choose between 1-6.");
                    startWork();
                    break;
            }
        }
    }
}
