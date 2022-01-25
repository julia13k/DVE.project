package api.console;

import com.geekhub.exceptions.*;
import com.geekhub.mylogger.*;
import com.geekhub.services.HomeworkService;

public class HomeworkMenu {
    private final HomeworkService homeworkService;
    private final ArgumentsScanner scanner;
    private final MyLogger logger;
    int option;

    public HomeworkMenu(HomeworkService homeworkService, ArgumentsScanner scanner, MyLogger logger) {
        this.homeworkService = homeworkService;
        this.scanner = scanner;
        this.logger = logger;
    }

    /** Main method for implementing homework menu */
    public void startHomeworkMenu() {
        printMenu();
        startWork();
    }
    /** Prints all options of a homework menu */
    private void printMenu() {
        System.out.print(new StringBuilder("Welcome to homework menu,")
            .append("please, press ENTER to continue\n"));
        scanner.getLine();

        System.out.println(new StringBuilder
            ("1 - Show all homeworks(number, name and role)\n")
            .append("2 - Add a homework\n")
            .append("3 - Delete a homework\n")
            .append("4 - Get a homework\n")
            .append("5 - Exit\n"));
    }
    /** The implementation of a homework menu options according to arguments received from scanner*/
    private void startWork() {
        System.out.print(new StringBuilder("You are using homework menu\n")
            .append("Please enter the number you have chosen:\n"));
        option = scanner.getNumber();
        while (!(option == 5)) {
            switch (option) {
                case 1:
                    homeworkService.showHomeworks();
                    startHomeworkMenu();
                    break;
                case 2:
                    System.out.println("Please enter the task and deadline(in format 12-31-2021 15:30:59:904) :");
                    scanner.getLine();
                    try {
                        homeworkService.createHomework(scanner.getLine(), scanner.getLine());
                    } catch (ValidationException e) {
                        logger.log(LoggerType.ERROR, e, "You have to type all the arguments");
                    }
                    startWork();
                    break;
                case 3:
                    homeworkService.showHomeworks();
                    System.out.println("Please enter a number of a homework you want to delete:");
                    homeworkService.deleteHomework(scanner.getNumber());
                    startHomeworkMenu();
                    break;
                case 4:
                    homeworkService.showHomeworks();
                    System.out.println("Please enter a number of a homework you want to get:");
                    homeworkService.getHomework(scanner.getNumber());
                    startHomeworkMenu();
                    break;
                default:
                    System.out.println("Wrong menu number! Choose between 1-5.");
                    startHomeworkMenu();
                    break;
            }
        }
    }
}
