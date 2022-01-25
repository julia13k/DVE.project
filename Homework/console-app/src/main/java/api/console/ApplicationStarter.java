package api.console;

import com.geekhub.mylogger.MyLogger;
import com.geekhub.services.*;

public class ApplicationStarter {

    private final GeneralMenu generalMenu;

    public ApplicationStarter(GeneralMenu generalMenu) {this.generalMenu = generalMenu;}

    public GeneralMenu getGeneralMenu() {
        return generalMenu;
    }

    /** Application starter method */
    public static void main(String[] args) {
        ArgumentsScanner scanner = new ArgumentsScanner();
        MyLogger logger = new MyLogger();
        CourseService courseService = new CourseService();
        HomeworkService homeworkService = new HomeworkService();
        LectionService lectionService = new LectionService();
        PersonService personService = new PersonService();
        ResourseService resourseService = new ResourseService();

        MyLoggerMenu myLoggerMenu = new MyLoggerMenu(scanner, logger);
        CourseMenu courseMenu = new CourseMenu(courseService, scanner, logger);
        HomeworkMenu homeworkMenu = new HomeworkMenu(homeworkService, scanner, logger);
        LectionMenu lectionMenu = new LectionMenu(resourseService, homeworkService, lectionService, scanner, logger);
        PersonMenu personMenu = new PersonMenu(personService, scanner, logger);
        ResourseMenu resourseMenu = new ResourseMenu(resourseService, scanner, logger);

        GeneralMenu generalMenu = new GeneralMenu(
                scanner, courseMenu, lectionMenu,personMenu, resourseMenu, homeworkMenu, myLoggerMenu);
        ApplicationStarter applicationStarter = new ApplicationStarter(generalMenu);
        applicationStarter.generalMenu.startGeneralMenu();
    }
}
