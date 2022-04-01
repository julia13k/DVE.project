package api.console;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.exceptions.*;
import com.geekhub.mylogger.*;
import com.geekhub.services.CourseService;
import com.geekhub.services.ResourseService;
import com.geekhub.sources.CourseSourse;
import com.geekhub.sources.ResourseSource;
import config.ConsoleConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileNotFoundException;

public class ResourseMenu {
    private final ArgumentsScanner scanner;
    private final MyLogger logger;
    int option;

    public ResourseMenu(ArgumentsScanner scanner, MyLogger logger) {
        this.scanner = scanner;
        this.logger = logger;
    }

    /** Main method for implementing resource menu */
    public void startResourseMenu() throws FileNotFoundException {
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

    /** Gets resource service */
    public ResourseService getResourseService() {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class, ConsoleConfig.class);
        applicationContext.refresh();
        var resourseService = applicationContext.getBean(ResourseService.class);
        return resourseService;
    }

    /** The implementation of a resource menu options according to arguments received from scanner*/
    private void startWork() throws FileNotFoundException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class, ConsoleConfig.class);
        applicationContext.refresh();
        var resourseService = applicationContext.getBean(ResourseService.class);
        var resourseSource = applicationContext.getBean(ResourseSource.class);
        if(resourseSource.showResourses().size() == 0) {
            resourseService.loadResourcesToList();
        }
        System.out.print(new StringBuilder("You are using additional materials menu\n")
                .append("Please enter the number you have chosen:\n"));
        option = scanner.getNumber();
        while (!(option == 5)) {
            switch (option) {
                case 1:
                    getResourseService().showResources();
                    startWork();
                    break;
                case 2:
                    System.out.println("Please enter title, data and type(BOOK/URL/VIDEO) of your resourse:");
                    scanner.getLine();
                    try {
                        getResourseService().createResourse(scanner.getLine(), scanner.getLine(), scanner.getLine());
                    } catch (ValidationException e) {
                        logger.log(LoggerType.ERROR, e, "You have to type all the arguments");
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
                    startWork();
                    break;
                case 3:
                    getResourseService().showResources();
                    System.out.println("Please enter a number of a resourse you want to delete:");
                    try {
                        getResourseService().deleteResourse(scanner.getNumber());
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
                    startWork();
                    break;
                case 4:
                    getResourseService().showResources();
                    System.out.println("Please enter a number of a resourse you want to get:");
                    try {
                        System.out.println(getResourseService().getResourse(scanner.getNumber()).getName());
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
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
