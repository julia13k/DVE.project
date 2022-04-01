package api.console;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.exceptions.*;
import com.geekhub.mylogger.*;
import com.geekhub.services.CourseService;
import com.geekhub.services.HomeworkService;
import com.geekhub.sources.CourseSourse;
import com.geekhub.sources.HomeworkSource;
import config.ConsoleConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileNotFoundException;

public class HomeworkMenu {
    private final ArgumentsScanner scanner;
    private final MyLogger logger;
    int option;

    public HomeworkMenu(ArgumentsScanner scanner, MyLogger logger) {
        this.scanner = scanner;
        this.logger = logger;
    }

    public void startHomeworkMenu() {
        printMenu();
        try {
            startWork();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    public HomeworkService getHomeworkService() {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class, ConsoleConfig.class);
        applicationContext.refresh();
        var homeworkService = applicationContext.getBean(HomeworkService.class);
        return homeworkService;
    }

    private void startWork() throws FileNotFoundException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class, ConsoleConfig.class);
        applicationContext.refresh();
        var homeworkService = applicationContext.getBean(HomeworkService.class);
        var homeworkSourse = applicationContext.getBean(HomeworkSource.class);
        if(homeworkSourse.showHomeworks().size() == 0) {
            homeworkService.loadHomeworksToList();
        }
        System.out.print(new StringBuilder("You are using homework menu\n")
                .append("Please enter the number you have chosen:\n"));
        option = scanner.getNumber();
        while (!(option == 5)) {
            switch (option) {
                case 1:
                    getHomeworkService().showHomeworks();
                    startHomeworkMenu();
                    break;
                case 2:
                    System.out.println("Please enter the task and deadline(in format 12-31-2021 15:30:59:904) :");
                    scanner.getLine();
                    try {
                        getHomeworkService().createHomework(scanner.getLine(), scanner.getLine());
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
                    getHomeworkService().showHomeworks();
                    System.out.println("Please enter a number of a homework you want to delete:");
                    try {
                        getHomeworkService().deleteHomework(scanner.getNumber());
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
                    startHomeworkMenu();
                    break;
                case 4:
                    getHomeworkService().showHomeworks();
                    System.out.println("Please enter a number of a homework you want to get:");
                    try {
                        System.out.println(getHomeworkService().getHomework(scanner.getNumber()).getTask());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
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
