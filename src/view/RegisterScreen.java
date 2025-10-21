package view;

import java.io.Console;
import java.util.Scanner;

import app.ScreenManager;
import service.VaultService;

public class RegisterScreen extends Screen {
    Console console = System.console();

    public RegisterScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
    }

    @Override
    public Screen show() {
        try {
            System.out.print("Nome de usuário: "); 
            String u = sc.nextLine();
            char[] pw = (console != null) ? console.readPassword("Senha mestra: ") : sc.nextLine().toCharArray();

            vaultService.register(u, pw);
            System.out.println("Usuário registrado (store atualizado).");

            System.out.println("\n\nPressione ENTER para continuar...");
            sc.nextLine();

            return new LoginScreen(screenManager, vaultService, sc);
        }
        catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());

            System.out.println("\n\nPressione ENTER para continuar...");
            sc.nextLine();

            return this;
        }

    }
}
