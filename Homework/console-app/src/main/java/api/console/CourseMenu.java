package api.console;

import com.geekhub.exceptions.*;
import com.geekhub.mylogger.*;
import com.geekhub.services.CourseService;

public class CourseMenu {
    private final CourseService courseService;
    private final ArgumentsScanner scanner;
    private final MyLogger logger;
    int option;

    public CourseMenu(CourseService courseService, ArgumentsScanner scanner, MyLogger logger) {
        this.courseService = courseService;
        this.scanner = scanner;
        this.logger = logger;
    }

    /** Main method for implementing course menu */
    public void startCourseMenu() {
        printMenu();
        startWork();
    }
    /** Prints all options of a course menu */
    private void printMenu() {
        System.out.print(new StringBuilder("Welcome to course menu,")
            .append("please, press ENTER to continue\n"));
        scanner.getLine();

        System.out.println(new StringBuilder
            ("1 - Show all courses(names)\n")
            .append("2 - Add a course\n")
            .append("3 - Delete a course\n")
            .append("4 - Get a course\n")
            .append("5 - Exit\n"));
    }
    /** The implementation of a course menu options according to arguments received from scanner*/
    private void startWork() {
        System.out.print(new StringBuilder("You are using course menu\n")
            .append("Please enter the number you have chosen:\n"));
        option = scanner.getNumber();
        while (!(option == 5)){
            switch (option){
                case 1:
                    courseService.showCourses();
                    startWork();
                    break;
                case 2:
                        System.out.println("Please enter the title of your course:");
                    scanner.getLine();
                    try {
                        courseService.createCourse(scanner.getLine());
                    } catch (ValidationException e) {
                        logger.log(LoggerType.ERROR, e, "You have to type all the arguments");
                    }
                    startWork();
                    break;
                case 3:
                    courseService.showCourses();
                    System.out.println("Please enter a number of a course you want to delete:");
                    courseService.deleteCourse(scanner.getNumber());
                    startWork();
                    break;
                case 4:
                    courseService.showCourses();
                    System.out.println("Please enter a number of a course you want to get:");
                    courseService.getCourse(scanner.getNumber());
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
