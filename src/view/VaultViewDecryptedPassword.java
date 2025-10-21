package view;

import java.util.Scanner;

import app.ScreenManager;
import service.VaultService;

public class VaultViewDecryptedPassword extends Screen {
    public VaultViewDecryptedPassword(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
    }
    
    @Override
    public Screen show() {
        try {
            System.out.print("ID da entrada: "); String id = sc.nextLine();
            String pwd = vaultService.viewDecryptedPassword(id);
            System.out.println("Senha: " + pwd);

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
