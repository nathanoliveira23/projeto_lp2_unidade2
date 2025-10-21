package app;

import java.util.Scanner;
import service.VaultService;
import view.LoginScreen;
import view.Screen;

public class ScreenManager {
    private final VaultService vaultService = new VaultService();
    private final Scanner sc = new Scanner(System.in);
    private boolean running = true;

    public void start() {
        Screen current = new LoginScreen(this, vaultService, sc);

        while (running) {
            current = current.show();
        }

    }

    public void stop() {
        running = false;
    }
}
