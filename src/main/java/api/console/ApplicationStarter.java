package api.console;

import mylogger.MyLogger;
import services.LectionService;

import java.util.Scanner;

public class ApplicationStarter {
    GeneralMenu generalMenu = new GeneralMenu();

    public static void main(String[] args) {
        ApplicationStarter applicationStarter = new ApplicationStarter();
        applicationStarter.generalMenu.startGeneralMenu();
    }
}
