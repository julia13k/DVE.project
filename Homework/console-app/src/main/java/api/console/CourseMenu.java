package api.console;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.exceptions.*;
import com.geekhub.mylogger.*;
import com.geekhub.services.CourseService;
import com.geekhub.sources.CourseSourse;
import config.ConsoleConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class CourseMenu {
    private final ArgumentsScanner scanner;
    private final MyLogger logger;
    int option;

    public CourseMenu(ArgumentsScanner scanner, MyLogger logger) {
        this.scanner = scanner;
        this.logger = logger;
    }

    public void startCourseMenu() throws FileNotFoundException {
        printMenu();
        startWork();
    }

    public void printMenu() {
        System.out.print(new StringBuilder("Welcome to course menu,")
                .append("please, press ENTER to continue"));
        scanner.getLine();

        System.out.println(new StringBuilder
                ("\n1 - Show all courses(names)\n")
                .append("2 - Add a course\n")
                .append("3 - Delete a course\n")
                .append("4 - Get a course\n")
                .append("5 - Exit\n"));
    }

    public CourseService getCourseService() {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class, ConsoleConfig.class);
        applicationContext.refresh();
        var courseService = applicationContext.getBean(CourseService.class);
        return courseService;
    }

    private void startWork() throws FileNotFoundException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class, ConsoleConfig.class);
        applicationContext.refresh();
        var courseService = applicationContext.getBean(CourseService.class);
        var courseSourse = applicationContext.getBean(CourseSourse.class);
        if(courseSourse.showCourses().size() == 0) {
            courseService.loadCoursesToList();
        }
        System.out.print(new StringBuilder("You are using course menu\n")
                .append("Please enter the number you have chosen:\n"));
        option = scanner.getNumber();
        while (!(option == 5)){
            switch (option){
                case 1:
                    getCourseService().showCourses();
                    startCourseMenu();
                    break;
                case 2:
                    System.out.println("Please enter the title of your course:");
                    scanner.getLine();
                    try {
                        getCourseService().createCourse(scanner.getLine());
                    } catch (ValidationException e) {
                        logger.log(LoggerType.ERROR, e, "You have to type all the arguments");
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
                    startCourseMenu();
                    break;
                case 3:
                    getCourseService().showCourses();
                    System.out.println("Please enter a number of a course you want to delete:");
                    try {
                        getCourseService().deleteCourse(scanner.getNumber());
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
                    startCourseMenu();
                    break;
                case 4:
                    getCourseService().showCourses();
                    System.out.println("Please enter a number of a course you want to get:");
                    try {
                        System.out.println(getCourseService().getCourse(scanner.getNumber()).getName());
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
                    startCourseMenu();
                    break;
                default:
                    System.out.println("Wrong menu number! Choose between 1-5.");
                    startCourseMenu();
                    break;
            }
        }
    }
}
