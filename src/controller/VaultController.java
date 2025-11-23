package controller;

import java.util.List;

import service.IVaultService;
import model.VaultEntry;

public class VaultController {
    private final IVaultService vaultService;
    private final int MAXLENGHT = 32;

    public VaultController(IVaultService vs) {
        this.vaultService = vs;
    }

    public void createEntry(String title, String username, String password, String url, String notes) throws Exception {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Informe um título válido");

        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Informe um nome de usuário válido");

        if (url == null || url.isBlank())
            throw new IllegalArgumentException("Informe uma URL válida");

        vaultService.addEntry(title, username, password, url, notes);
    }

    public String generatePassword(int length) {
        if (length <= 0)
            throw new IllegalArgumentException("O tamanho da senha deve ser um valor positivo.");

        if (length > MAXLENGHT)
            throw new IllegalArgumentException("O tamanho da senha não pode ser maior que " + MAXLENGHT + " caracteres.");

        return vaultService.generatePassword(length);
    }

    public String generatePassword(String input) {
        if (input == null || input.isBlank())
            throw new IllegalArgumentException("Este campo não pode estar vazio");

        if (input.length() > MAXLENGHT)
            throw new IllegalArgumentException("O tamanho do texto não dever ser maior que " + MAXLENGHT + " caracteres.");

        return vaultService.generatePassword(input);
    }

    public List<VaultEntry> listEntries() {
        return vaultService.listEntries();
    }

    public void removeEntry(String id) throws Exception {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("É necessário informar o ID da entrada.");

        vaultService.removeEntry(id);
    }

    public void updateEntry(String id, String title, String username, String password, String url, String notes) throws Exception {
        vaultService.updateEntry(id, title.isBlank() ? null : title, 
                username.isBlank() ? null : username, 
                password.isBlank() ? null : password, 
                url.isBlank() ? null : url, 
                notes.isBlank() ? null : notes);
    }

    public String viewPassword(String id) throws Exception {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("É necessário informar o ID da entrada.");

        return vaultService.viewDecryptedPassword(id);
    }
}
