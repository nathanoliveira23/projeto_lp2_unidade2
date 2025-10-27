package app;

import java.util.Scanner;
import service.VaultService;
import util.ConsoleUtil;
import view.AuthenticationScreen;
import view.Logo;
import view.Screen;

public class ScreenManager {
    private final VaultService vaultService = new VaultService();
    private final Scanner sc = new Scanner(System.in);
    private boolean running = true;

    public void start() {
        Screen current = new AuthenticationScreen(this, vaultService, sc);

        while (running) {
            ConsoleUtil.clearScreen();
            Logo.showLogo();
            current = current.show();
        }
    }

    public void stop() {
        running = false;
    }
}
