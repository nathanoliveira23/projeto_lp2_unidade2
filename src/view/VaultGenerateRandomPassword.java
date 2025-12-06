package view;

import java.util.Scanner;

import app.ScreenManager;
import controller.VaultController;
import service.IVaultService;
import service.VaultService;
import util.ConsoleUtil;

public class VaultGenerateRandomPassword extends Screen {
    private final VaultController vaultController;

    public VaultGenerateRandomPassword(ScreenManager sm, IVaultService vs, Scanner sc) {
        super(sm, vs, sc);
        this.vaultController = new VaultController(vs);
    }

    @Override
    public Screen show() {
        try {
            printMenuHeader("Gerador de senha aleatória");

            printInputMessage("Tamanho"); 
            int len = tryParseOption(sc.nextLine());

            if (len == -1)
                return showInvalidOption();

            String gen = vaultController.generatePassword(len);

            System.out.println(">>> Senha gerada: " + gen);

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
