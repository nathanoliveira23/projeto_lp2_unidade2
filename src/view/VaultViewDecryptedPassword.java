package view;

import java.util.Scanner;

import app.ScreenManager;
import controller.VaultController;
import service.IVaultService;
import util.ConsoleUtil;

public class VaultViewDecryptedPassword extends Screen {
    private final VaultController vaultController;

    public VaultViewDecryptedPassword(ScreenManager sm, IVaultService vs, Scanner sc) {
        super(sm, vs, sc);
        this.vaultController = new VaultController(vs);
    }
    
    @Override
    public Screen show() {
        try {
            printMenuHeader("Vizualizar Senha");

            printInputMessage("ID da entrada"); 
            String id = sc.nextLine();

            String pwd = vaultController.viewPassword(id);

            System.out.println(">>> Senha: " + pwd);

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
