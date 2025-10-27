package util;

import java.util.Scanner;

public class ConsoleUtil {
    public static void waitForEnter(Scanner scanner) {
        System.out.println(Color.BRIGHT_WHITE.apply("\n\nPressione ENTER para continuar..."));
        scanner.nextLine();
    }
    
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
