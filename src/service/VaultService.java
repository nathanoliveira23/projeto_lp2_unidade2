package service;

import model.User;
import model.VaultEntry;
import repository.IUserRepository;
import repository.IVaultRepository;
import repository.UserRepository;
import repository.VaultRepository;
import util.CryptoUtil;
import util.PasswordGenerator;

import javax.crypto.SecretKey;

import exception.AuthenticationException;
import exception.EntryNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VaultService implements IVaultService {
    private final IUserRepository userRepository;
    private final IVaultRepository vaultRepository;

    private Map<String, VaultEntry> entries = new LinkedHashMap<>();
    private User currentUser = null;
    private SecretKey privateKey = null;

    public VaultService(IUserRepository userRepository, IVaultRepository vaultRepository) throws IOException {
        this.userRepository = userRepository;
        this.vaultRepository = vaultRepository;
    }

    public void register(String username, char[] masterPassword) throws Exception {
        User user = userRepository.findUserByName(username);

        if (user != null) 
            throw new AuthenticationException("Já existe usuário cadastrado.");

        byte[] salt = CryptoUtil.newSalt();
        String passwdStr = new String(masterPassword);
        SecretKey key = CryptoUtil.deriveKey(masterPassword, salt);
        String verifier = CryptoUtil.SHA256(passwdStr, salt);

        currentUser = new User(username, salt, verifier);
        userRepository.addUser(currentUser);

        privateKey = key;
    }

    public void login(String username, char[] masterPassword) throws Exception {
        User user = userRepository.findUserByName(username);

        if (user == null || !user.getUsername().equals(username)) 
            throw new AuthenticationException("Usuário não existe.");

        currentUser = user;

        String passwdStr = new String(masterPassword);
        String candidateHash = CryptoUtil.SHA256(passwdStr, currentUser.getSalt());

        if (!candidateHash.equals(currentUser.getVerifierHash()))
            throw new AuthenticationException("Senha mestra incorreta.");

        SecretKey secretKey = CryptoUtil.deriveKey(masterPassword, currentUser.getSalt());
        Arrays.fill(masterPassword, '\0');

        privateKey = secretKey;

        List<VaultEntry> entriesDB = vaultRepository.loadVault(currentUser.getUsername());

        entries.clear();

        for (VaultEntry entry : entriesDB)
            this.entries.put(entry.getId(), entry);
    }

    public void updateUsername(String newUsername) throws Exception {
        if (currentUser == null)
            throw new AuthenticationException("Nenhum usuário logado.");

        User existing = userRepository.findUserByName(newUsername);
        if (existing != null)
            throw new IllegalArgumentException("Nome de usuário já está em uso.");

        List<User> users = userRepository.findAll();
        for (User u : users) {
            if (u.getUsername().equals(currentUser.getUsername())) {
                // Atualiza o nome de usuário
                users.remove(u);
                users.add(new User(newUsername, u.getSalt(), u.getVerifierHash()));
                break;
            }
        }

        userRepository.updateAll(users);

        Path oldFile = Paths.get("data/vaults", currentUser.getUsername() + ".csv");
        Path newFile = Paths.get("data/vaults", newUsername + ".csv");

        if (Files.exists(oldFile)) {
            Files.move(oldFile, newFile, StandardCopyOption.REPLACE_EXISTING);
        }

        currentUser = new User(newUsername, currentUser.getSalt(), currentUser.getVerifierHash());
    }

    public void updatePassword(char[] newPassword) throws Exception {
        if (currentUser == null)
            throw new AuthenticationException("Nenhum usuário logado.");

        byte[] salt = CryptoUtil.newSalt();
        SecretKey newKey = CryptoUtil.deriveKey(newPassword, salt);
        String verifier = CryptoUtil.SHA256(new String(newPassword), salt);
        Arrays.fill(newPassword, '\0');

        List<User> users = userRepository.findAll();

        for (User u : users) {
            if (u.getUsername().equals(currentUser.getUsername())) {
                users.remove(u);
                users.add(new User(currentUser.getUsername(), salt, verifier));
                break;
            }
        }

        userRepository.updateAll(users);

        currentUser = new User(currentUser.getUsername(), salt, verifier);

        privateKey = newKey;
    }

    public void deleteAccount() throws Exception {
        if (currentUser == null)
            throw new AuthenticationException("Nenhum usuário logado.");

        String username = currentUser.getUsername();

        userRepository.deleteUser(username);

        Path file = Paths.get("data/vaults", username + ".csv");
        Files.deleteIfExists(file);

        currentUser = null;
        privateKey = null;
        entries.clear();
    }


    public void addEntry(String title, String usernameEntry, String plainPassword, String url, String notes) throws Exception {
        if (privateKey == null) 
            throw new AuthenticationException("Não autenticado.");

        String encrypted = CryptoUtil.encrypt(plainPassword, privateKey);
        VaultEntry e = new VaultEntry(title, usernameEntry, encrypted, url, notes);
        entries.put(e.getId(), e);
        
        vaultRepository.saveVault(currentUser.getUsername(), new ArrayList<>(entries.values()));
    }

    public List<VaultEntry> listEntries() { return new ArrayList<>(entries.values()); }

    public String viewDecryptedPassword(String id) throws Exception {
        VaultEntry e = entries.get(id);

        if (e == null) 
            throw new EntryNotFoundException("Entrada não encontrada.");

        if (privateKey == null) 
            throw new AuthenticationException("Não autenticado.");

        return CryptoUtil.decrypt(e.getEncryptedPassword(), privateKey);
    }

    public void updateEntry(String id, String title, String usernameEntry, String plainPassword, String url, String notes) throws Exception {
        VaultEntry e = entries.get(id);

        if (e == null) 
            throw new EntryNotFoundException("Entrada não encontrada.");

        if (title != null) e.setTitle(title);
        if (usernameEntry != null) e.setUsername(usernameEntry);
        if (plainPassword != null) e.setEncryptedPassword(CryptoUtil.encrypt(plainPassword, privateKey));
        if (url != null) e.setUrl(url);
        if (notes != null) e.setNotes(notes);

        vaultRepository.saveVault(currentUser.getUsername(), new ArrayList<>(entries.values()));
    }

    public void removeEntry(String id) throws Exception {
        if (entries.remove(id) == null) 
            throw new EntryNotFoundException("Entrada não encontrada.");

        vaultRepository.saveVault(currentUser.getUsername(), new ArrayList<>(entries.values()));
    }

    public String generatePassword(int length) {
        PasswordGenerator pg = new PasswordGenerator();

        return pg.generate(length);
    }

    public String generatePassword(String text) {
        PasswordGenerator pg = new PasswordGenerator();
        
        return pg.scramblePassword(text);
    }
}
