package view;

import java.util.Scanner;

import app.ScreenManager;
import controller.VaultController;
import service.IVaultService;
import service.VaultService;
import util.ConsoleUtil;

public class VaultAddEntryScreen extends Screen {
    private final VaultController vaultController;

    public VaultAddEntryScreen(ScreenManager sm, IVaultService vs, Scanner sc) {
        super(sm, vs, sc);
        this.vaultController = new VaultController(vs);
    }

    @Override
    public Screen show() {
        try {
            printMenuHeader("Adicionar Entrada");

            printInputMessage("Título (ex: Gmail)"); 
            String title = sc.nextLine();

            printInputMessage("Usuário"); 
            String uname = sc.nextLine();

            printInputMessage("Senha (ou deixe vazio para gerar)"); 
            String pass = sc.nextLine();

            if (pass.isBlank()) {
                printInputMessage("Tamanho da senha"); 
                int len = Integer.parseInt(sc.nextLine());

                pass = vaultController.generatePassword(len);

                System.out.println(">>> Senha gerada: " + pass);
            }

            printInputMessage("URL"); 
            String url = sc.nextLine();

            printInputMessage("Notas"); 
            String notes = sc.nextLine();

            vaultController.createEntry(title, uname, pass, url, notes);

            systemMessage(MessageType.SUCCESS, "Entrada adicionada com sucesso");
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
