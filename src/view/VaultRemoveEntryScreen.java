package view;

import java.util.Scanner;

import app.ScreenManager;
import controller.VaultController;
import service.IVaultService;
import service.VaultService;
import util.ConsoleUtil;

public class VaultRemoveEntryScreen extends Screen {
    private final VaultController vaultController;

    public VaultRemoveEntryScreen(ScreenManager sm, IVaultService vs, Scanner sc) {
        super(sm, vs, sc);
        this.vaultController = new VaultController(vs);
    }

    @Override
    public Screen show() {
        try {
            printMenuHeader("Remover Entrada");

            printInputMessage("ID da entrada"); 
            String id = sc.nextLine();

            vaultController.removeEntry(id);

            systemMessage(MessageType.SUCCESS, "Entrada removida com sucesso");

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
