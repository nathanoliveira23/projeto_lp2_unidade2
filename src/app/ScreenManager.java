package app;

import java.util.Scanner;

import repository.IUserRepository;
import repository.IVaultRepository;
import service.VaultService;
import util.ConsoleUtil;
import view.AuthenticationScreen;
import view.Logo;
import view.Screen;

public class ScreenManager {
    private VaultService vaultService;
    private IUserRepository userRepository;
    private IVaultRepository vaultRepository;
    private final Scanner sc = new Scanner(System.in);
    private boolean running = true;

    public ScreenManager() {
        try {
            this.vaultService = new VaultService(userRepository, vaultRepository);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

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
