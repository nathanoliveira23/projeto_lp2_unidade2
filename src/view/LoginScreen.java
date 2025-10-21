package view;

import java.util.Scanner;
import java.io.Console;
import app.ScreenManager;
import service.VaultService;

public class LoginScreen extends Screen {
    Console console = System.console();

    public LoginScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
    }

    @Override
    public Screen show() {
        System.out.println("1 - Registrar usuário (apenas 1)"); 
        System.out.println("2 - Login"); 
        System.out.println("0 - Sair"); 
        System.out.print("\nOpção: ");

        int option = Integer.parseInt(sc.nextLine());

        try {
            return switch (option) {
                case 1 -> {
                    yield new RegisterScreen(screenManager, vaultService, sc);
                }
                case 2 -> {
                    System.out.print("Nome de usuário: "); String u2 = sc.nextLine();
                    char[] pw2 = (console != null) ? console.readPassword("Senha mestra: ") : sc.nextLine().toCharArray();
                    vaultService.login(u2, pw2);
                    System.out.println("Login bem-sucedido.");

                    System.out.println("\n\nPressione ENTER para continuar...");
                    sc.nextLine();

                    yield new VaultScreen(screenManager, vaultService, sc);
                }
                case 0 -> {
                    screenManager.stop();
                    yield this;
                }
                default -> this;
            };
        }
        catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());

            System.out.println("\n\nPressione ENTER para continuar...");
            sc.nextLine();

            return this;
        }
    }
}
