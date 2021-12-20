package api.console;

import exceptions.ValidationException;
import models.Lection;
import mylogger.LoggerType;
import mylogger.MyLogger;
import services.LectionService;
import services.ResourseService;

import java.util.Scanner;

public class LectionMenu {

    LectionService lectionService = new LectionService();
    ResourseService resourseService = new ResourseService();
    ArgumentsScanner scanner = new ArgumentsScanner();
    MyLogger logger = new MyLogger();
    int option;

    public LectionMenu(LectionService lectionService) {
        this.lectionService = lectionService;
    }

    public void startLectionMenu() {
        printMenu();
        startWork();

    }

    private void printMenu() {
        System.out.print(new StringBuilder("Welcome to lection menu,")
            .append("please, press ENTER to continue"));
        scanner.getLine();

        System.out.println(new StringBuilder
            ("1 - Show all lections(number and name)\n")
            .append("2 - Add a lecture\n")
            .append("3 - Delete a lecture\n")
            .append("4 - Get a lecture\n")
            .append("5 - Add a person to a lecture\n")
            .append("6 - Add a resourse to a lecture\n")
            .append("7 - Add a homework to a lecture\n")
            .append("8 - Show an additional material by a lecture\n")
            .append("9 - Show homework by a lecture\n")
            .append("10 - Exit\n"));
    }

    private void startWork() {
        System.out.print(new StringBuilder("You are using lections menu\n")
            .append("Please enter the number you have chosen:\n"));
        option = scanner.getNumber();
        while (!(option == 10)) {
            switch (option){
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
                    System.out.println("Please enter a number of lection you want to delete:");
                    lectionService.deleteLection(scanner.getNumber());
                    startWork();
                    break;
                case 4:
                    lectionService.showLections();
                    System.out.println("Please enter a number of lection you want to get:");
                    lectionService.getLection(scanner.getNumber());
                    startWork();
                    break;
                case 5:
                    System.out.println("Please enter index of a lection and a teacher(appropriately) that you want to connect:");
                    lectionService.addTeacher(scanner.getNumber(), scanner.getNumber());
                    startWork();
                    break;
                case 6:
                    System.out.println("Please enter index of a lection and a resourse(appropriately) that you want to connect:");
                    lectionService.addResource(scanner.getNumber(), scanner.getNumber());
                    startWork();
                    break;
                case 7:
                    System.out.println("Please enter index of a lection and a homework(appropriately) that you want to connect:");
                    lectionService.addHomework(scanner.getNumber(), scanner.getNumber());
                    startWork();
                    break;
                case 8:
                    System.out.println("Additional material by lections:");
                    lectionService.showResourcesByLection();
                    startWork();
                    break;
                case 9:
                    System.out.println("Homework by lections:");
                    lectionService.showHomeworksByLection();
                    startWork();
                    break;
                default:
                    System.out.println("Wrong menu number! Choose between 1-6.");
                    startWork();
                    break;
            }
            logger.getAllLogs();
        }
    }
}
