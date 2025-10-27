package view;

import java.util.Scanner;

import app.ScreenManager;
import controller.VaultController;
import service.VaultService;
import util.ConsoleUtil;

public class VaultGenerateScrambledPassword extends Screen {
    private final VaultController vaultController;

    public VaultGenerateScrambledPassword(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
        this.vaultController = new VaultController(vs);
    }
    
    @Override
    public Screen show() {
        try {
            printMenuHeader("Transformador de Senhas");
            printInputMessage("Informe uma palavra");
            String rawPassword = sc.nextLine();

            String scrambledPassword = vaultController.generatePassword(rawPassword);

            System.out.println(">>> Senha fortificada: " + scrambledPassword);

            ConsoleUtil.waitForEnter(sc);

            return new VaultGeneratePasswordScreen(screenManager, vaultService, sc);
        }
        catch (Exception e) {
            systemMessage(MessageType.ERROR, e.getMessage());
            ConsoleUtil.waitForEnter(sc);

            return this;
        }
    }
}
