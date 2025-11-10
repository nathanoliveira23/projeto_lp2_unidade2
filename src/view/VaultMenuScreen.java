package view;

import java.util.Scanner;

import app.ScreenManager;
import service.VaultService;

public class VaultMenuScreen extends Screen {
    public VaultMenuScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
    }

    @Override
    public Screen show() {
        printMenuHeader("Menu Principal");

        printMenuOption(1, "Adicionar entrada");
        printMenuOption(2, "Listar entradas");
        printMenuOption(3, "Ver senha de uma entrada");
        printMenuOption(4, "Atualizar entrada");
        printMenuOption(5, "Remover entrada");
        printMenuOption(6, "Gerar senha aleatória");
        printMenuOption(7, "Configuração de conta");
        printMenuOption(0, "Logout\n");

        printInputMessage("Opção");
        int option = tryParseOption(sc.nextLine());

        if (option == -1)
            return showInvalidOption();

        try {
            switch (option) {
                case 1: return new VaultAddEntryScreen(screenManager, vaultService, sc);
                case 2: return new VaultListEntries(screenManager, vaultService, sc);
                case 3: return new VaultViewDecryptedPassword(screenManager, vaultService, sc);
                case 4: return new VaultUpdateEntryScreen(screenManager, vaultService, sc);
                case 5: return new VaultRemoveEntryScreen(screenManager, vaultService, sc);
                case 6: return new VaultGeneratePasswordScreen(screenManager, vaultService, sc);
                case 7: return new AccountSettingsScreen(screenManager, vaultService, sc);
                case 0: 
                    screenManager.stop();
                    return null;
                default: 
                    return showInvalidOption();
            }
        }
        catch (Exception e) {
            systemMessage(MessageType.ERROR, e.getMessage());
            return this;
        }
    }
}
