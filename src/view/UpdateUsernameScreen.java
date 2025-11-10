package view;

import java.util.Scanner;

import app.ScreenManager;
import controller.UserController;
import service.VaultService;

public class UpdateUsernameScreen extends Screen {
    private final UserController userController;

    public UpdateUsernameScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm,vs, sc);
        this.userController = new UserController(vs);
    }

    @Override
    public Screen show() {
        try {
            printMenuHeader("Atualizar nome de usuário");

            printInputMessage("Informe o novo nome de usuário");
            
            String newUsername = sc.nextLine();

            userController.updateUsername(newUsername);

            systemMessage(MessageType.SUCCESS, "Nome de usuário atualizado com sucesso");

            return new AccountSettingsScreen(screenManager, vaultService, sc);
        }
        catch (Exception e) {
            systemMessage(MessageType.ERROR, e.getMessage());
            return this;
        }
    }
}
