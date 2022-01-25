package api.console;

import com.geekhub.exceptions.*;
import com.geekhub.mylogger.*;
import com.geekhub.services.ResourseService;

public class ResourseMenu {
    private final ResourseService resourseService;
    private final ArgumentsScanner scanner;
    private final MyLogger logger;
    int option;

    public ResourseMenu(ResourseService resourseService, ArgumentsScanner scanner, MyLogger logger) {
        this.resourseService = resourseService;
        this.scanner = scanner;
        this.logger = logger;
    }

    /** Main method for implementing resource menu */
    public void startResourseMenu() {
        printMenu();
        startWork();
    }
    /** Prints all options of a resource menu */
    private void printMenu() {
        System.out.print(new StringBuilder("Welcome to resourse menu,")
            .append("please, press ENTER to continue"));
        scanner.getLine();

        System.out.println(new StringBuilder
            ("1 - Show all resourses(number and title)\n")
            .append("2 - Add a resourse\n")
            .append("3 - Delete a resourse\n")
            .append("4 - Get a resourse\n")
            .append("5 - Exit\n"));
    }
    /** The implementation of a resource menu options according to arguments received from scanner*/
    private void startWork() {
        System.out.print(new StringBuilder("You are using additional materials menu\n")
            .append("Please enter the number you have chosen:\n"));
        option = scanner.getNumber();
        while (!(option == 5)) {
            switch (option) {
                case 1:
                    resourseService.showResources();
                    startWork();
                    break;
                case 2:
                    System.out.println("Please enter title, data and type(BOOK/URL/VIDEO) of your resourse:");
                    scanner.getLine();
                    try {
                        resourseService.createResourse(scanner.getLine(), scanner.getLine(), scanner.getLine());
                    } catch (ValidationException e) {
                        logger.log(LoggerType.ERROR, e, "You have to type all the arguments");
                    }
                    startWork();
                    break;
                case 3:
                    resourseService.showResources();
                    System.out.println("Please enter a number of a resourse you want to delete:");
                    resourseService.deleteResourse(scanner.getNumber());
                    startWork();
                    break;
                case 4:
                    resourseService.showResources();
                    System.out.println("Please enter a number of a resourse you want to get:");
                    resourseService.getResourse(scanner.getNumber());
                    startWork();
                    break;
                default:
                    System.out.println("Wrong menu number! Choose between 1-5.");
                    startWork();
                    break;
            }
        }
    }
}
