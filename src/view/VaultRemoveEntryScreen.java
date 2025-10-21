package view;

import java.util.Scanner;

import app.ScreenManager;
import service.VaultService;

public class VaultRemoveEntryScreen extends Screen {
    public VaultRemoveEntryScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
    }

    @Override
    public Screen show() {
        try {
            System.out.print("ID da entrada: "); 
            String idr = sc.nextLine();

            vaultService.removeEntry(idr);

            System.out.println("Removido.");

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
