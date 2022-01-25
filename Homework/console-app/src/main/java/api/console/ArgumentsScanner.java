package api.console;

import java.util.Scanner;

public class ArgumentsScanner {
    private Scanner scanner = new Scanner(System.in);

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
    /** Gets String arguments from a user */
    public String getLine() {
        return scanner.nextLine();
    }
    /** Gets int arguments from a user */
    public int getNumber() {
        return scanner.nextInt();
    }
}
