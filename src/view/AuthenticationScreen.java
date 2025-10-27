package view;

import java.util.Scanner;

import app.ScreenManager;
import service.VaultService;
import util.ConsoleUtil;

public class AuthenticationScreen extends Screen {

    public AuthenticationScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
    }

    @Override
    public Screen show() {
        printMenuHeader("Autenticação");

        printMenuOption(1, "Cadastrar usuário");
        printMenuOption(2, "Login");
        printMenuOption(0, "Sair\n");

        printInputMessage("Opção");
        int option = tryParseOption(sc.nextLine());

        if (option == -1)
            return showInvalidOption();

        try {
            switch (option) {
                case 1: 
                    return new RegisterScreen(screenManager, vaultService, sc);
                case 2:
                    return new LoginScreen(screenManager, vaultService, sc);
                case 0:
                    screenManager.stop();
                    return null;
                default:
                    return showInvalidOption();
            }
        }
        catch (Exception e) {
            systemMessage(MessageType.ERROR, e.getMessage());
            ConsoleUtil.waitForEnter(sc);

            return null;
        }
    }
}
