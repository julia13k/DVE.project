package api.console;

import com.geekhub.exceptions.*;
import com.geekhub.mylogger.*;
import com.geekhub.services.HomeworkService;
import com.geekhub.services.LectionService;
import com.geekhub.services.ResourseService;
import com.geekhub.sources.HomeworkSource;
import com.geekhub.sources.ResourseSource;

public class LectionMenu {

    private final ResourseService resourseService;
    private final HomeworkService homeworkService;
    private final LectionService lectionService;
    private final ArgumentsScanner scanner;
    private final MyLogger logger;
    int option;

    public LectionMenu(ResourseService resourseService, HomeworkService homeworkService, LectionService lectionService, ArgumentsScanner scanner, MyLogger logger) {
        this.resourseService = resourseService;
        this.homeworkService = homeworkService;
        this.lectionService = lectionService;
        this.scanner = scanner;
        this.logger = logger;
    }

    /** Main method for implementing lecture menu */
    public void startLectionMenu() {
        printMenu();
        startWork();

    }
    /** Prints all options of a lecture menu */
    private void printMenu() {
        System.out.print(new StringBuilder("Welcome to a lecture menu,")
            .append("please, press ENTER to continue"));
        scanner.getLine();

        System.out.println(new StringBuilder
            ("1 - Show all lectures(number and name)\n")
            .append("2 - Add a lecture\n")
            .append("3 - Delete a lecture\n")
            .append("4 - Get a lecture\n")
            .append("5 - Add a person to a lecture\n")
            .append("6 - Add a homework to a lecture\n")
            .append("7 - Add a resource to a lecture\n")
            .append("8 - Show an additional material by a lecture\n")
            .append("9 - Show homework by a lecture\n")
            .append("10 - Exit\n"));
    }
    /** The implementation of a lecture menu options according to arguments received from scanner*/
    private void startWork() {
        System.out.print(new StringBuilder("You are using lectures menu\n")
            .append("Please enter the number you have chosen:\n"));
        option = scanner.getNumber();
        while (option != 10) {
            switch (option) {
                case 1:
                    lectionService.showLections();
                    startWork();
                    break;
                case 2:
                    System.out.println("Please enter the title and description of your lecture:");
                    scanner.getLine();
                    try {
                        lectionService.createLection(scanner.getLine(), scanner.getLine());
                    } catch (ValidationException e) {
                        logger.log(LoggerType.ERROR, e, "You have to type all the arguments");
                    }
                    startWork();
                    break;
                case 3:
                    lectionService.showLections();
                    System.out.println("Please enter a number of lecture you want to delete:");
                    lectionService.deleteLection(scanner.getNumber());
                    startWork();
                    break;
                case 4:
                    lectionService.showLections();
                    System.out.println("Please enter a number of lecture you want to get:");
                    lectionService.getLection(scanner.getNumber());
                    startWork();
                    break;
                case 5:
                    System.out.println("Please enter index of a lecture and a teacher(appropriately) that you want to connect:");
                    lectionService.addTeacher(scanner.getNumber(), scanner.getNumber());
                    startWork();
                    break;
                case 6:
                    lectionService.showLections();
                    System.out.println("Please enter the index of lecture where do you want to add homework(s):");
                    int lectureIndex = scanner.getNumber();
                    homeworkService.showHomeworks();
                    System.out.println(
                        "Please enter the number of homeworks that you want to connect to a lecture:");
                    int homeworkIndex = scanner.getNumber();
                    lectionService.addHomework(lectureIndex, homeworkIndex);
                    startWork();
                    break;
                case 7:
                    lectionService.showLections();
                    System.out.println("Please enter the index of lecture where do you want to add resource(s):");
                    int lectionIndex = scanner.getNumber();
                    resourseService.showResources();
                    System.out.println(
                        "Please enter the number of homeworks that you want to connect to a lecture:");
                    int resourceIndex = scanner.getNumber();
                    lectionService.addResource(lectionIndex, resourceIndex);
                    startWork();
                    break;
                case 8:
                    System.out.println("Additional material by lectures:");
                    lectionService.groupByResource().forEach((key, value) ->
                        System.out.printf("%s - %s \n", key, value));
                    startWork();
                    break;
                case 9:
                    System.out.println("Homework by lectures:");
                    lectionService.groupByHomework().forEach((key, value) ->
                        System.out.printf("%s - %s \n", key, value));
                    startWork();
                    break;
                default:
                    System.out.println("Wrong menu number! Choose between 1-6.");
                    startWork();
                    break;
            }
        }
    }
}
