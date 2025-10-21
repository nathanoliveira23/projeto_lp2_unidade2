package view;

import java.util.Scanner;

import app.ScreenManager;
import service.VaultService;

public class VaultAddEntryScreen extends Screen {
    public VaultAddEntryScreen(ScreenManager sm, VaultService vs, Scanner sc) {
        super(sm, vs, sc);
    }

    @Override
    public Screen show() {
        try {
            System.out.print("Título (ex: Gmail): "); 
            String title = sc.nextLine();
            System.out.print("Usuário: "); 
            String uname = sc.nextLine();
            System.out.print("Senha (ou deixe vazio para gerar): "); 
            String pass = sc.nextLine();

            if (pass.isBlank()) {
                System.out.print("Tamanho da senha: "); int len = Integer.parseInt(sc.nextLine());
                pass = vaultService.generatePassword(len);
                System.out.println("Senha gerada: " + pass);
            }

            System.out.print("URL: "); String url = sc.nextLine();
            System.out.print("Notas: "); String notes = sc.nextLine();
            vaultService.addEntry(title, uname, pass, url, notes);
            System.out.println("Entrada adicionada.");

            System.out.println("\n\nPressione ENTER para continuar...");
            sc.nextLine();

            return new VaultScreen(screenManager, vaultService, sc);
        }
        catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return this;
        }
    }
}
