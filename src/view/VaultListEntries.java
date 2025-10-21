package view;

import java.util.Scanner;
import java.util.List;

import app.ScreenManager;
import model.VaultEntry;
import service.VaultService;

public class VaultListEntries extends Screen {
    public VaultListEntries(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
    }

    @Override
    public Screen show() {
        try {
            List<VaultEntry> list = vaultService.listEntries();

            if (list.isEmpty()) 
                System.out.println("Nenhuma entrada.");
            else 
                list.forEach(System.out::println);

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
