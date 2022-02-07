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
        CourseService courseService = new CourseService(logger);
        HomeworkService homeworkService = new HomeworkService(logger);
        LectionService lectionService = new LectionService(logger);
        PersonService personService = new PersonService(logger);
        ResourseService resourseService = new ResourseService(logger);

        MyLoggerMenu myLoggerMenu = new MyLoggerMenu(scanner, logger);
        CourseMenu courseMenu = new CourseMenu(courseService, scanner, logger);
        HomeworkMenu homeworkMenu = new HomeworkMenu(homeworkService, scanner, logger);
        LectionMenu lectionMenu = new LectionMenu(resourseService, homeworkService, lectionService, scanner, logger);
        PersonMenu personMenu = new PersonMenu(personService, scanner, logger);
        ResourseMenu resourseMenu = new ResourseMenu(resourseService, scanner, logger);

        GeneralMenu generalMenu = new GeneralMenu(
                scanner, courseMenu, lectionMenu,personMenu, resourseMenu, homeworkMenu, myLoggerMenu);
        ApplicationStarter applicationStarter = new ApplicationStarter(generalMenu);
        applicationStarter.generalMenu.getStorageTypeFromUser();
        applicationStarter.generalMenu.startGeneralMenu();
    }
}
