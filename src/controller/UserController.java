package controller;

import java.util.Arrays;

import service.VaultService;

public class UserController {
    private final VaultService vaultService;
    
    public UserController(VaultService vs) {
        this.vaultService = vs;
    }

    public void register(String username, char[] password) throws Exception {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("É necessario informar um nome de usuário.");

        if (username.contains(" "))
            throw new IllegalArgumentException("O nome de usuário não deve conter espaços em branco.");

        String passwordStr = new String(password);

        if (passwordStr == null || passwordStr.isBlank())
            throw new IllegalArgumentException("É necessario informar uma senha válida.");

        vaultService.register(username, password);
        Arrays.fill(password, '\0');
    }

    public void login(String username, char[] masterPassword) throws Exception {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Nome de usuário não pode estar vazio.");

        if (masterPassword == null || masterPassword.length == 0)
            throw new IllegalArgumentException("Senha não pode estar vazia.");

        vaultService.login(username, masterPassword);
        Arrays.fill(masterPassword, '\0');
    }
}
