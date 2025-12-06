package view;

import java.util.Scanner;

import app.ScreenManager;
import controller.UserController;
import service.IVaultService;

public class UpdatePasswordScreen extends Screen {
    private final UserController userController;

    public UpdatePasswordScreen(ScreenManager sm, IVaultService vs, Scanner sc) {
        super(sm,vs, sc);
        this.userController = new UserController(vs);
    }

    @Override
    public Screen show() {
        try {
            printMenuHeader("Atualizar senha de usuário");

            printInputMessage("Informe a nova senha");
            
            char[] newPasssword = sc.nextLine().toCharArray();

            userController.updatePassword(newPasssword);

            systemMessage(MessageType.SUCCESS, "Senha de usuário atualizada com sucesso");

            return new AccountSettingsScreen(screenManager, vaultService, sc);
        }
        catch (Exception e) {
            systemMessage(MessageType.ERROR, e.getMessage());
            return this;
        }
    }
}
