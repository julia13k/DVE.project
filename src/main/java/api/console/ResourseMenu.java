package api.console;

import exceptions.ValidationException;
import mylogger.LoggerType;
import mylogger.MyLogger;
import services.PersonService;
import services.ResourseService;

import java.util.Scanner;

public class ResourseMenu {
    ResourseService resourseService = new ResourseService();
    ArgumentsScanner scanner = new ArgumentsScanner();
    MyLogger logger = new MyLogger();
    int option;

    public ResourseMenu(ResourseService resourseService) {
        this.resourseService = resourseService;
    }

    public void startResourseMenu() {
        printMenu();
        startWork();
    }

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
            logger.getAllLogs();
        }
    }
}
