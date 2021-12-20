package src.main.api.console;

public class ApplicationStarter {
    GeneralMenu generalMenu = new GeneralMenu();

    public static void main(String[] args) {
        ApplicationStarter applicationStarter = new ApplicationStarter();
        applicationStarter.generalMenu.startGeneralMenu();
    }
}
