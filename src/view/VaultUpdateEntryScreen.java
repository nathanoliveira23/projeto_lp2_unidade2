package view;

import java.io.Console;
import java.util.Scanner;

import app.ScreenManager;
import service.VaultService;

public class VaultUpdateEntryScreen extends Screen {
    private Console console = System.console();

    public VaultUpdateEntryScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
    }

    @Override
    public Screen show() {
        try {
            System.out.print("ID da entrada: "); String idu = sc.nextLine();
            System.out.print("Novo título (ou vazio): "); String nt = sc.nextLine();
            System.out.print("Novo usuário (ou vazio): "); String nu = sc.nextLine();
            System.out.print("Nova senha (ou vazio): "); String np = sc.nextLine();
            System.out.print("Nova URL (ou vazio): "); String nuurl = sc.nextLine();
            System.out.print("Novas notas (ou vazio): "); String nn = sc.nextLine();

            vaultService.updateEntry(idu, nt.isBlank() ? null : nt, 
                    nu.isBlank()?null:nu, 
                    np.isBlank()?null:np, 
                    nuurl.isBlank()?null:nuurl, 
                    nn.isBlank()?null:nn);

            System.out.println("Atualizado.");

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
