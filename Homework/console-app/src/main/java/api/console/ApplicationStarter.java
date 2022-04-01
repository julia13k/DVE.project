package api.console;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import config.ConsoleConfig;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.io.FileNotFoundException;

public class ApplicationStarter {
    private final GeneralMenu generalMenu;

    /** Application starter method */
    public ApplicationStarter(GeneralMenu generalMenu) {
        this.generalMenu = generalMenu;
    }

    public static void main(String[] args) {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class, ConsoleConfig.class);
        applicationContext.refresh();
        Flyway flyway = (Flyway)applicationContext.getBean("flyway");
        flyway.migrate();
        var applicationStarter = new ApplicationStarter(applicationContext.getBean(GeneralMenu.class));
        applicationStarter.generalMenu.getStorageTypeFromUser();
        try {
            applicationStarter.generalMenu.startGeneralMenu();
        } catch (FileNotFoundException e) {
            System.err.println("There is no file!");
            e.printStackTrace();
        }
    }
}
