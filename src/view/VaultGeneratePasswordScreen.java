package view;

import java.util.Scanner;

import app.ScreenManager;
import service.VaultService;

public class VaultGeneratePasswordScreen extends Screen {
    public VaultGeneratePasswordScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
    }

    @Override
    public Screen show() {
        try {
            System.out.print("Tamanho: "); int len = Integer.parseInt(sc.nextLine());
            String gen = vaultService.generatePassword(len);
            System.out.println("Senha gerada: " + gen);

            System.out.println("\n\nPressione ENTER para continuar...");
            sc.nextLine();

            return new VaultScreen(screenManager, vaultService, sc);
        }
        catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());

            System.out.println("\n\nPressione ENTER para continuar...");
            sc.nextLine();

            return this;
        }
    }
}
