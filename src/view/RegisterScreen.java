package view;

import java.io.Console;
import java.util.Scanner;

import app.ScreenManager;
import controller.UserController;
import service.VaultService;
import util.Color;
import util.ConsoleUtil;

public class RegisterScreen extends Screen {
    Console console = System.console();
    private final UserController userController;

    public RegisterScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
        this.userController = new UserController(vs);
    }

    @Override
    public Screen show() {
        try {
            printMenuHeader("Cadastro");
            printInputMessage("Nome de usuário"); 
            String u = sc.nextLine();

            char[] pw = (console != null) 
                ? console.readPassword(Color.BRIGHT_WHITE.apply(">>> Senha mestra: ")) 
                : sc.nextLine().toCharArray();

            userController.register(u, pw);

            systemMessage(MessageType.SUCCESS, "Usuário registrado com sucesso");

            ConsoleUtil.waitForEnter(sc);

            return new AuthenticationScreen(screenManager, vaultService, sc);
        }
        catch (Exception e) {
            systemMessage(MessageType.ERROR, e.getMessage());
            ConsoleUtil.waitForEnter(sc);

            return this;
        }
    }
}
