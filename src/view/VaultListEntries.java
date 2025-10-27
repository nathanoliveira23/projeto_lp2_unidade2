package view;

import java.util.Scanner;
import java.util.List;

import app.ScreenManager;
import controller.VaultController;
import model.VaultEntry;
import service.VaultService;
import util.ConsoleUtil;

public class VaultListEntries extends Screen {
    private final VaultController vaultController;

    public VaultListEntries(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
        this.vaultController = new VaultController(vs);
    }

    @Override
    public Screen show() {
        try {
            printMenuHeader("Listar Entradas");

            List<VaultEntry> list = vaultController.listEntries();

            if (list.isEmpty()) 
                systemMessage(MessageType.WARNING, "Não há nenhuma entrada no cofre.");
            else 
                list.forEach(System.out::println);

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
