package view;

import java.util.Scanner;

import app.ScreenManager;
import service.VaultService;

public class AccountSettingsScreen extends Screen {

    public AccountSettingsScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
    }

    @Override
    public Screen show() {
        printMenuHeader("Configurações de conta");

        printMenuOption(1, "Atualizar username");
        printMenuOption(2, "Atualizar senha");
        printMenuOption(3, "Deletar conta");
        printMenuOption(0, "Voltar para o menu\n");

        printInputMessage("Opção");
        int option = tryParseOption(sc.nextLine());

        if (option == -1)
            return showInvalidOption();

        try {
            switch (option) {
                case 1: 
                    return new UpdateUsernameScreen(screenManager, vaultService, sc);
                case 2:
                    return new UpdatePasswordScreen(screenManager, vaultService, sc);
                case 3:
                    return new DeleteAccountScreen(screenManager, vaultService, sc);
                case 0:
                    return new VaultMenuScreen(screenManager, vaultService, sc);
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
