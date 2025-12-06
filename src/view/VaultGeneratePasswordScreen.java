package view;

import java.util.Scanner;

import app.ScreenManager;
import service.IVaultService;
import service.VaultService;
import util.ConsoleUtil;

public class VaultGeneratePasswordScreen extends Screen {
    public VaultGeneratePasswordScreen(ScreenManager sm, IVaultService vs, Scanner sc) {
        super(sm, vs, sc);
    }

    @Override
    public Screen show() {
        try {
            printMenuHeader("Gerador de senha aleatória");

            printMenuOption(1, "Gerar senha aleatória");
            printMenuOption(2, "Fortificar senha");
            printMenuOption(0, "Voltar para o menu\n");

            printInputMessage("Opção");
            int option = tryParseOption(sc.nextLine());

            if (option == -1) 
                return showInvalidOption();

            switch (option) {
                case 1: return new VaultGenerateRandomPassword(screenManager, vaultService, sc);
                case 2: return new VaultGenerateScrambledPassword(screenManager, vaultService, sc);
                case 0: return new VaultMenuScreen(screenManager, vaultService, sc);
                default: return showInvalidOption();
            }
        }
        catch (Exception e) {
            systemMessage(MessageType.ERROR, e.getMessage());
            ConsoleUtil.waitForEnter(sc);

            return this;
        }
    }
}
