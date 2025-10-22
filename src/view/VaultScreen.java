package view;

import java.util.Scanner;

import app.ScreenManager;
import service.VaultService;

public class VaultScreen extends Screen {
    public VaultScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
    }

    @Override
    public Screen show() {
        System.out.println("[1] - Adicionar entrada"); 
        System.out.println("[2] - Listar entradas"); 
        System.out.println("[3] - Ver senha de uma entrada"); 
        System.out.println("[4] - Atualizar entrada"); 
        System.out.println("[5] - Remover entrada"); 
        System.out.println("[6] - Gerar senha aleatória"); 
        System.out.println("[0] - Logout"); 
        System.out.print("\n>>> Opção: ");
        int option = Integer.parseInt(sc.nextLine());

        try {
            switch (option) {
                case 1: return new VaultAddEntryScreen(screenManager, vaultService, sc);
                case 2: return new VaultListEntries(screenManager, vaultService, sc);
                case 3: return new VaultViewDecryptedPassword(screenManager, vaultService, sc);
                case 4: return new VaultUpdateEntryScreen(screenManager, vaultService, sc);
                case 5: return new VaultRemoveEntryScreen(screenManager, vaultService, sc);
                case 6: return new VaultGeneratePasswordScreen(screenManager, vaultService, sc);
                case 0: {
                    screenManager.stop();
                    return null;
                }
                default: return this;
            }
        }
        catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return this;
        }
    }
}
