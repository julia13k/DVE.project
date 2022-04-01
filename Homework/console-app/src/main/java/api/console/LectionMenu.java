package api.console;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.exceptions.*;
import com.geekhub.mylogger.*;
import com.geekhub.services.LectionService;
import com.geekhub.sources.HomeworkSource;
import com.geekhub.sources.LectionSource;
import com.geekhub.sources.ResourseSource;
import config.ConsoleConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileNotFoundException;

public class LectionMenu {
    private final ArgumentsScanner scanner;
    private final MyLogger logger;
    int option;

    public LectionMenu(ArgumentsScanner scanner, MyLogger logger) {
        this.scanner = scanner;
        this.logger = logger;
    }

    public void startLectionMenu() throws FileNotFoundException {
        printMenu();
        startWork();
    }

    public LectionService getLectionService() {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class, ConsoleConfig.class);
        applicationContext.refresh();
        var lectionService = applicationContext.getBean(LectionService.class);
        return lectionService;
    }

    public ResourseSource getResourseSource() {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class, ConsoleConfig.class);
        applicationContext.refresh();
        var resourseSource = applicationContext.getBean(ResourseSource.class);
        return resourseSource;
    }

    public HomeworkSource getHomeworkSource() {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class, ConsoleConfig.class);
        applicationContext.refresh();
        var homeworkSource = applicationContext.getBean(HomeworkSource.class);
        return homeworkSource;
    }

    private void printMenu() {
        System.out.print(new StringBuilder("Welcome to lecture menu,")
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

    private void startWork() throws FileNotFoundException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class, ConsoleConfig.class);
        applicationContext.refresh();
        var lectionService = applicationContext.getBean(LectionService.class);
        var lectionSource = applicationContext.getBean(LectionSource.class);
        if(lectionSource.showLections().size() == 0) {
            lectionService.loadLecturesToList();
        }
        System.out.print(new StringBuilder("You are using lections menu\n")
                .append("Please enter the number you have chosen:\n"));
        option = scanner.getNumber();
        while (option != 10) {
            switch (option) {
                case 1:
                    getLectionService().showLections();
                    startWork();
                    break;
                case 2:
                    System.out.println("Please enter the title and description of your lecture:");
                    scanner.getLine();
                    try {
                        getLectionService().createLection(scanner.getLine(), scanner.getLine());
                    } catch (ValidationException e) {
                        logger.log(LoggerType.ERROR, e, "You have to type all the arguments");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
                    startWork();
                    break;
                case 3:
                    getLectionService().showLections();
                    System.out.println("Please enter a number of lecture you want to delete:");
                    try {
                        getLectionService().deleteLection(scanner.getNumber());
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
                    startWork();
                    break;
                case 4:
                    getLectionService().showLections();
                    System.out.println("Please enter a number of lecture you want to get:");
                    try {
                        System.out.println(getLectionService().getLection(scanner.getNumber()).getName());
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
                    startWork();
                    break;
                case 5:
                    System.out.println(
                            "Please enter index of a lecture and a teacher(appropriately) that you want to connect:");
                    try {
                        getLectionService().addTeacher(scanner.getNumber(), scanner.getNumber());
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
                    startWork();
                    break;
                case 6:
                    getLectionService().showLections();
                    System.out.println("Please enter the index of lecture where do you want to add homework(s):");
                    try {
                        int lectureIndex = scanner.getNumber();
                        getHomeworkSource().showHomeworks();
                        System.out.println(
                                "Please enter the number of homeworks that you want to connect to a lecture:");
                        int homeworkIndex = scanner.getNumber();
                        getLectionService().addHomework(lectureIndex, homeworkIndex);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
                    startWork();
                    break;
                case 7:
                    getLectionService().showLections();
                    System.out.println("Please enter the index of lecture where do you want to add resource(s):");
                    try {
                        int lectionIndex = scanner.getNumber();
                        getResourseSource().showResourses();
                        System.out.println(
                                "Please enter the number of homeworks that you want to connect to a lecture:");
                        int resourceIndex = scanner.getNumber();
                        getLectionService().addResource(lectionIndex, resourceIndex);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
                    startWork();
                    break;
                case 8:
                    System.out.println("Additional material by lectures:");
                    getLectionService().groupByResource().forEach((key, value) ->
                            System.out.printf("%s - %s \n", key, value));
                    startWork();
                    break;
                case 9:
                    System.out.println("Homework by lectures:");
                    getLectionService().groupByHomework().forEach((key, value) ->
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
