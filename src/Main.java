import exception.*;
import model.VaultEntry;

import java.util.*;
import java.io.Console;
import service.VaultService;

public class Main {
    public static void main(String[] args) {
        VaultService vs = new VaultService();
        Scanner sc = new Scanner(System.in);
        Console console = System.console();

        while (true) {
            System.out.println("\n=== PASSMAN (Terminal) ===");
            System.out.println("1 - Registrar usuário (apenas 1)"); 
            System.out.println("2 - Login"); 
            System.out.println("3 - Adicionar entrada"); 
            System.out.println("4 - Listar entradas"); 
            System.out.println("5 - Ver senha de uma entrada"); 
            System.out.println("6 - Atualizar entrada"); 
            System.out.println("7 - Remover entrada"); 
            System.out.println("8 - Gerar senha aleatória"); 
            System.out.println("0 - Sair"); 
            System.out.print("Opção: ");
            int op = Integer.parseInt(sc.nextLine());
            
            try {
                switch (op) {
                    case 1 -> {
                        System.out.print("Nome de usuário: "); String u = sc.nextLine();
                        char[] pw = (console != null) ? console.readPassword("Senha mestra: ") : sc.nextLine().toCharArray();
                        vs.register(u, pw);
                        System.out.println("Usuário registrado (store atualizado).");
                    }
                    case 2 -> {
                        System.out.print("Nome de usuário: "); String u2 = sc.nextLine();
                        char[] pw2 = (console != null) ? console.readPassword("Senha mestra: ") : sc.nextLine().toCharArray();
                        vs.login(u2, pw2);
                        System.out.println("Login bem-sucedido.");
                    }
                    case 3 -> {
                        System.out.print("Título (ex: Gmail): "); String title = sc.nextLine();
                        System.out.print("Usuário: "); String uname = sc.nextLine();
                        System.out.print("Senha (ou deixe vazio para gerar): "); String pass = sc.nextLine();

                        if (pass.isBlank()) {
                            System.out.print("Tamanho da senha: "); int len = Integer.parseInt(sc.nextLine());
                            pass = vs.generatePassword(len);
                            System.out.println("Senha gerada: " + pass);
                        }

                        System.out.print("URL: "); String url = sc.nextLine();
                        System.out.print("Notas: "); String notes = sc.nextLine();
                        vs.addEntry(title, uname, pass, url, notes);
                        System.out.println("Entrada adicionada.");
                    }
                    case 4 -> {
                        List<VaultEntry> list = vs.listEntries();
                        if (list.isEmpty()) 
                            System.out.println("Nenhuma entrada.");
                        else 
                            list.forEach(System.out::println);
                    }
                    case 5 -> {
                        System.out.print("ID da entrada: "); String id = sc.nextLine();
                        String pwd = vs.viewDecryptedPassword(id);
                        System.out.println("Senha: " + pwd);
                    }
                    case 6 -> {
                        System.out.print("ID da entrada: "); String idu = sc.nextLine();
                        System.out.print("Novo título (ou vazio): "); String nt = sc.nextLine();
                        System.out.print("Novo usuário (ou vazio): "); String nu = sc.nextLine();
                        System.out.print("Nova senha (ou vazio): "); String np = sc.nextLine();
                        System.out.print("Nova URL (ou vazio): "); String nuurl = sc.nextLine();
                        System.out.print("Novas notas (ou vazio): "); String nn = sc.nextLine();
                        vs.updateEntry(idu, nt.isBlank()?null:nt, nu.isBlank()?null:nu, np.isBlank()?null:np, nuurl.isBlank()?null:nuurl, nn.isBlank()?null:nn);
                        System.out.println("Atualizado.");
                    }
                    case 7 -> {
                        System.out.print("ID da entrada: "); String idr = sc.nextLine();
                        vs.removeEntry(idr);
                        System.out.println("Removido.");
                    }
                    case 8 -> {
                        System.out.print("Tamanho: "); int len = Integer.parseInt(sc.nextLine());
                        String gen = vs.generatePassword(len);
                        System.out.println("Senha gerada: " + gen);
                    }
                    case 0 -> { System.out.println("Encerrando..."); sc.close(); return; }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (Exception ex) { 
                System.out.println("Erro: " + ex.getMessage()); 
            }
        }
    }
}
