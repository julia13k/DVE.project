package api.console;

import exceptions.ValidationException;
import mylogger.LoggerType;
import mylogger.MyLogger;
import services.HomeworkService;
import services.PersonService;

public class HomeworkMenu {
    HomeworkService homeworkService = new HomeworkService();
    ArgumentsScanner scanner = new ArgumentsScanner();
    MyLogger logger = new MyLogger();
    int option;

    public HomeworkMenu(HomeworkService homeworkService) {
        this.homeworkService = homeworkService;
    }

    public void startHomeworkMenu() {
        printMenu();
        startWork();
    }

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
                    System.out.println("Please enter the task and deadline:");
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
            logger.getAllLogs();
        }
    }
}
