package api.console;

import com.geekhub.config.AppConfig;
import com.geekhub.config.DatabaseConfig;
import com.geekhub.exceptions.*;
import com.geekhub.mylogger.*;
import com.geekhub.services.PersonService;
import com.geekhub.sources.PersonSourse;
import config.ConsoleConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileNotFoundException;

public class PersonMenu {
    private final ArgumentsScanner scanner;
    private final MyLogger logger;
    int option;

    public PersonMenu(ArgumentsScanner scanner, MyLogger logger) {
        this.scanner = scanner;
        this.logger = logger;
    }

    /** Main method for implementing person menu */
    public void startPersonMenu() throws FileNotFoundException {
        printMenu();
        startWork();
    }

    /** Gets person service */
    public PersonService getPersonService() {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class, ConsoleConfig.class);
        applicationContext.refresh();
        var personService = applicationContext.getBean(PersonService.class);
        return personService;
    }

    /** Prints all options of a person menu */
    private void printMenu() {
        System.out.print(new StringBuilder("Welcome to person menu,")
                .append("please, press ENTER to continue\n"));
        scanner.getLine();

        System.out.println(new StringBuilder
                ("1 - Show all people(number, name and role)\n")
                .append("2 - Add a person\n")
                .append("3 - Delete a person\n")
                .append("4 - Get a person\n")
                .append("5 - Exit\n"));
    }

    /** The implementation of a person menu options according to arguments received from scanner*/
    private void startWork() throws FileNotFoundException {
        var applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(DatabaseConfig.class, AppConfig.class, ConsoleConfig.class);
        applicationContext.refresh();
        var personService = applicationContext.getBean(PersonService.class);
        var personSourse = applicationContext.getBean(PersonSourse.class);
        if(personSourse.showPeople().size() == 0) {
            personService.loadPeopleToList();
        }
        System.out.print(new StringBuilder("You are using person menu\n")
                .append("Please enter the number you have chosen:\n"));
        option = scanner.getNumber();
        while (!(option == 5)) {
            switch (option) {
                case 1:
                    getPersonService().showPeople();
                    startPersonMenu();
                    break;
                case 2:
                    System.out.println("Please enter first name, last name, contacts, email and role(TEACHER/STUDENT):");
                    scanner.getLine();
                    try {
                        getPersonService().createPerson(scanner.getLine(), scanner.getLine(),
                                scanner.getLine(), scanner.getLine(), scanner.getLine());
                    } catch (ValidationException e) {
                        logger.log(LoggerType.ERROR, e, "You have to type all the arguments");
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
                    startWork();
                    break;
                case 3:
                    getPersonService().showPeople();
                    System.out.println("Please enter a number of a person you want to delete:");
                    try {
                        getPersonService().deletePerson(scanner.getNumber());
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}
                    startPersonMenu();
                    break;
                case 4:
                    getPersonService().showPeople();
                    System.out.println("Please enter a number of a person you want to get:");
                    try {
                        System.out.println(getPersonService().getPerson(scanner.getNumber()).getFirstName() + " " +
                                getPersonService().getPerson(scanner.getNumber()).getLastName());
                    } catch (Exception e) {
                        System.err.println("This method has failed!");
                        logger.log(LoggerType.ERROR, e, "This method is failed");}

                    startPersonMenu();
                    break;
                default:
                    System.out.println("Wrong menu number! Choose between 1-5.");
                    startPersonMenu();
                    break;
            }
        }
    }
}
