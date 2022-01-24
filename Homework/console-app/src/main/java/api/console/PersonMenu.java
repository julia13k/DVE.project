package api.console;

import com.geekhub.exceptions.*;
import com.geekhub.mylogger.*;
import com.geekhub.services.PersonService;

public class PersonMenu {
    PersonService personService = new PersonService();
    ArgumentsScanner scanner = new ArgumentsScanner();
    MyLogger logger = new MyLogger();
    int option;

    public PersonMenu(PersonService personService) {
        this.personService = personService;
    }

    public void startPersonMenu() {
        printMenu();
        startWork();
    }

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

    private void startWork() {
        System.out.print(new StringBuilder("You are using person menu\n")
            .append("Please enter the number you have chosen:\n"));
        option = scanner.getNumber();
        while (!(option == 5)) {
            switch (option) {
                case 1:
                    personService.showPeople();
                    startPersonMenu();
                    break;
                case 2:
                    System.out.println("Please enter first name, last name, contacts, email and role(TEACHER/STUDENT):");
                    scanner.getLine();
                    try {
                        personService.createPerson(scanner.getLine(), scanner.getLine(), scanner.getLine(), scanner.getLine(), scanner.getLine());
                    } catch (ValidationException e) {
                        logger.log(LoggerType.ERROR, e, "You have to type all the arguments");
                    }
                    startWork();
                    break;
                case 3:
                    personService.showPeople();
                    System.out.println("Please enter a number of a person you want to delete:");
                    personService.deletePerson(scanner.getNumber());
                    startPersonMenu();
                    break;
                case 4:
                    personService.showPeople();
                    System.out.println("Please enter a number of a person you want to get:");
                    personService.getPerson(scanner.getNumber());
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
