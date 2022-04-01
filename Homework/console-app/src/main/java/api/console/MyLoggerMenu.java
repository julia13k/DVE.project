package api.console;

import com.geekhub.mylogger.MyLogger;

public class MyLoggerMenu {
    private final ArgumentsScanner scanner;
    private final MyLogger logger;
    int option;

    public MyLoggerMenu(ArgumentsScanner scanner, MyLogger logger) {
        this.scanner = scanner;
        this.logger = logger;
    }

    public MyLogger getLogger() {
        return logger;
    }

    public void startLoggerMenu() {
        if(!logger.getFile().exists()) {
            logger.createFile();
        }
        printMenu();
        startWork();
    }

    private void printMenu() {
        System.out.print(new StringBuilder("Welcome to Logger menu,")
                .append("please, press ENTER to continue\n"));
        scanner.getLine();

        System.out.println(new StringBuilder
                ("1 - Show all logs\n")
                .append("2 - Sort logs by date\n")
                .append("3 - Select logs by status\n")
                .append("4 - Exit\n"));
    }

    private void startWork() {
        logger.readFromFile(logger.getFile());
        System.out.print(new StringBuilder("You are using Logger menu\n")
                .append("Please enter the number you have chosen:\n"));
        option = scanner.getNumber();
        while (!(option == 4)) {
            switch (option) {
                case 1:
                    logger.getLogs().forEach(System.out::println);
                    startLoggerMenu();
                    break;
                case 2:
                    logger.sortLogsByDate();
                    startWork();
                    break;
                case 3:
                    logger.selectLogsByStatus();
                    startLoggerMenu();
                    break;
                default:
                    System.out.println("Wrong menu number! Choose between 1-5.");
                    startLoggerMenu();
                    break;
            }
        }
    }
}
