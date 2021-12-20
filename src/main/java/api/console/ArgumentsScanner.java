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

    public String getLine() {
        return scanner.nextLine();
    }

    public int getNumber() {
        return scanner.nextInt();
    }
}
