package view;

import java.util.Scanner;
import java.io.Console;

import app.ScreenManager;
import controller.UserController;
import service.VaultService;
import util.ConsoleUtil;

public class LoginScreen extends Screen {
    private final Console console = System.console();
    private final UserController userController;

    public LoginScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
        this.userController = new UserController(vs);
    }

    @Override
    public Screen show() {
        try {
            printMenuHeader("Login");

            printInputMessage("Nome de usuário"); 
            String username = sc.nextLine();

            char[] password;

            if (console != null) {
                printInputMessage("Senha mestre");
                password = console.readPassword();
            }
            else {
                password = sc.nextLine().toCharArray();
            }

            userController.login(username, password);

            //System.out.println(Color.BRIGHT_GREEN.apply("\n>>> Login bem-sucedido <<<"));
            systemMessage(MessageType.SUCCESS, "Login bem-sucedido");

            ConsoleUtil.waitForEnter(sc);

            return new VaultMenuScreen(screenManager, vaultService, sc);
        }
        catch (Exception e) {
            systemMessage(MessageType.ERROR, e.getMessage());
            ConsoleUtil.waitForEnter(sc);

            return this;
        }
    }
}
