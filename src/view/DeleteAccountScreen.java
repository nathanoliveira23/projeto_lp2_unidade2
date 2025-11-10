package view;

import java.util.Scanner;

import app.ScreenManager;
import controller.UserController;
import service.VaultService;
import util.ConsoleUtil;

public class DeleteAccountScreen extends Screen {
    private final UserController userController;

    public DeleteAccountScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm,vs, sc);
        this.userController = new UserController(vs);
    }

    @Override
    public Screen show() {
        try {
            printMenuHeader("Excluir conta");

            while (true) {
                printInputMessage("Tem certeza que deseja excluir sua conta? [Y/N]");
                String option = sc.nextLine().trim();

                if (option.equalsIgnoreCase("Y")) {
                    userController.deleteAccount();

                    systemMessage(MessageType.SUCCESS, "Sua conta foi excluída com sucesso!");
                    ConsoleUtil.waitForEnter(sc);

                    return new AuthenticationScreen(screenManager, vaultService, sc);
                }
                else if (option.equalsIgnoreCase("N")) {
                    return new AccountSettingsScreen(screenManager, vaultService, sc);
                }
                else {
                    showInvalidOption();
                }
            }
        }
        catch (Exception e) {
            systemMessage(MessageType.ERROR, e.getMessage());
            return this;
        }
    }
}
