package view;

import java.util.Scanner;

import app.ScreenManager;
import controller.VaultController;
import service.VaultService;
import util.ConsoleUtil;

public class VaultUpdateEntryScreen extends Screen {
    private final VaultController vaultController;

    public VaultUpdateEntryScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
        this.vaultController = new VaultController(vs);
    }

    @Override
    public Screen show() {
        try {
            printMenuHeader("Atualizar Entrada");

            printInputMessage("ID da entrada"); 
            String idu = sc.nextLine();

            printInputMessage("Novo título (ou vazio)"); 
            String nt = sc.nextLine();

            printInputMessage("Novo usuário (ou vazio)"); 
            String nu = sc.nextLine();

            printInputMessage("Nova senha (ou vazio)"); 
            String np = sc.nextLine();

            printInputMessage("Nova URL (ou vazio)"); 
            String nuurl = sc.nextLine();

            printInputMessage("Novas notas (ou vazio)"); 
            String nn = sc.nextLine();

            vaultController.updateEntry(idu, nt, nu, np, nuurl, nn);

            systemMessage(MessageType.SUCCESS, "Entrada atualizada com sucesso");

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
