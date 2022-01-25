package api.console;

import com.geekhub.services.CourseService;

public class ApplicationStarter {

    private final GeneralMenu generalMenu;

    public ApplicationStarter(GeneralMenu generalMenu) {this.generalMenu = generalMenu;}

    public GeneralMenu getGeneralMenu() {
        return generalMenu;
    }

    /** Application starter method */
    public static void main(String[] args) {
        ApplicationStarter applicationStarter = new ApplicationStarter();
        applicationStarter.generalMenu.startGeneralMenu();
    }
}
