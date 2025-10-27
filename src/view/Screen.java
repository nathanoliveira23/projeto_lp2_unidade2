package view;

import service.VaultService;

import java.util.Scanner;

import app.ScreenManager;
import util.Color;
import util.ConsoleUtil;

public abstract class Screen {
    protected final ScreenManager screenManager;
    protected final VaultService vaultService;
    protected final Scanner sc;

    protected enum MessageType { SUCCESS, ERROR, WARNING }

    public Screen(ScreenManager screenManager, VaultService vaultService, Scanner sc) {
        this.screenManager = screenManager;
        this.vaultService = vaultService;
        this.sc = sc;
    }

    public abstract Screen show();

    protected void printMenuOption(int digit, String text) {
        System.out.println(
            Color.BRIGHT_BLUE.apply("   [") + 
            Color.BRIGHT_WHITE.apply(String.valueOf(digit)) + 
            Color.BRIGHT_BLUE.apply("]") + 
            "  " + 
            Color.BRIGHT_WHITE.apply(text)
        );
    }

    protected void printMenuHeader(String title) {
        System.out.println(Color.BRIGHT_CYAN.apply("=-------[ " + title + " ]-------="));
        System.out.println();
        System.out.println();
    }

    protected void printInputMessage(String msg) {
        System.out.print(Color.BRIGHT_WHITE.apply(">>> " + msg + ": "));
    }

    protected void systemMessage(MessageType type, String msg) {
        if (type == MessageType.ERROR)
            System.out.println(Color.BRIGHT_RED.apply("MSG: [ " + msg + " ]"));
        else if (type == MessageType.SUCCESS)
            System.out.println(Color.BRIGHT_GREEN.apply("MSG: [ " + msg + " ]"));
        else if (type == MessageType.WARNING)
            System.out.println(Color.BRIGHT_YELLOW.apply("MSG: [ " + msg + " ]"));
    }

    protected Screen showInvalidOption() {
        systemMessage(MessageType.ERROR, "Opção inválida! Tente novamente.");
        ConsoleUtil.waitForEnter(sc);
        return this;
    }

    protected int tryParseOption(String option) {
        try {
            return Integer.parseInt(option);
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }
}
