package service;

import model.*;
import util.*;
import exception.*;
import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Base64;

public class VaultService {
    private Map<String, VaultEntry> entries = new LinkedHashMap<>();
    private User currentUser = null;
    private SecretKey privateKey = null;
    private static final Path STORE = Paths.get("vault_store.txt");

    // Register: only one user supported in this simple model
    public void register(String username, char[] masterPassword) throws Exception {
        if (Files.exists(STORE)) 
            loadStore();

        if (currentUser != null) 
            throw new AuthenticationException("Usuário já registrado.");

        byte[] salt = CryptoUtil.newSalt();
        String passwdStr = new String(masterPassword);
        String verifier = CryptoUtil.SHA256(passwdStr, salt);
        SecretKey key = CryptoUtil.deriveKey(masterPassword, salt);
//        String verifier = Base64.getEncoder().encodeToString(key.getEncoded());
        currentUser = new User(username, salt, verifier);
        privateKey = key;

        saveStore();
    }

    public void login(String username, char[] masterPassword) throws Exception {
        loadStore();

        if (currentUser == null || !currentUser.getUsername().equals(username)) 
            throw new AuthenticationException("Usuário não existe.");

        String passwdStr = new String(masterPassword);
        String candidateHash = CryptoUtil.SHA256(passwdStr, currentUser.getSalt());

        System.out.println("Login: " + currentUser.getVerifierHash());

        if (!candidateHash.equals(currentUser.getVerifierHash()))
            throw new AuthenticationException("Senha mestra incorreta.");

        SecretKey secretKey = CryptoUtil.deriveKey(masterPassword, currentUser.getSalt());
//        String candidateHash = Base64.getEncoder().encodeToString(candidate.getEncoded());
//
//        if (!candidateHash.equals(currentUser.getVerifierHash())) 
//            throw new AuthenticationException("Senha mestra incorreta.");
//
        privateKey = secretKey;
    }

    public void addEntry(String title, String usernameEntry, String plainPassword, String url, String notes) throws Exception {
        if (privateKey == null) 
            throw new AuthenticationException("Não autenticado.");

        String encrypted = CryptoUtil.encrypt(plainPassword, privateKey);
        VaultEntry e = new VaultEntry(title, usernameEntry, encrypted, url, notes);
        entries.put(e.getId(), e);

        saveStore();
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

        saveStore();
    }

    public void removeEntry(String id) throws Exception {
        if (entries.remove(id) == null) 
            throw new EntryNotFoundException("Entrada não encontrada.");

        saveStore();
    }

    public String generatePassword(int length) {
        PasswordGenerator pg = new PasswordGenerator();

        return pg.generate(length);
    }

    // Persistence: simple custom format (no external deps)
    private void saveStore() throws PersistenceException {
        try (BufferedWriter w = Files.newBufferedWriter(STORE)) {
            if (currentUser != null) {
                w.write("USER|" + escape(currentUser.getUsername()) + "|" 
                        + Base64.getEncoder().encodeToString(currentUser.getSalt()) 
                        + "|" + escape(currentUser.getVerifierHash()));

                w.newLine();
            } else {
                w.write("USER|null"); 
                w.newLine();
            }

            for (VaultEntry e : entries.values()) {
                w.write("ENTRY|" + escape(e.getId()) 
                                + "|" + escape(e.getTitle()) 
                                + "|" + escape(e.getUsername()) + "|" 
                                + escape(e.getEncryptedPassword()) + "|" 
                                + escape(e.getUrl()) + "|" + escape(e.getNotes()));
                w.newLine();
            }
        } catch (Exception ex) { 
            throw new PersistenceException("Erro ao salvar store", ex); 
        }
    }

    private void loadStore() throws PersistenceException {
        entries.clear();

        if (!Files.exists(STORE)) 
            return;

        try (BufferedReader r = Files.newBufferedReader(STORE)) {
            String line;

            while ((line = r.readLine()) != null) {
                if (line.startsWith("USER|")) {
                    String[] parts = line.split("\\|", 4);
                    if (parts.length >= 4 && !parts[1].equals("null")) {
                        String username = unescape(parts[1]);
                        byte[] salt = Base64.getDecoder().decode(parts[2]);
                        String verifier = unescape(parts[3]);
                        currentUser = new User(username, salt, verifier);
                    }
                } else if (line.startsWith("ENTRY|")) {
                    String[] parts = line.split("\\|", 8);
                    // ENTRY|id|title|username|encryptedPassword|url|notes
                    String id = unescape(parts[1]);
                    String title = unescape(parts[2]);
                    String uname = unescape(parts[3]);
                    String enc = unescape(parts[4]);
                    String url = unescape(parts[5]);
                    String notes = parts.length>6?unescape(parts[6]):"";
                    VaultEntry e = new VaultEntry(title, uname, enc, url, notes);
                    // override id to keep same
                    java.lang.reflect.Field f = VaultEntry.class.getDeclaredField("id"); f.setAccessible(true); f.set(e, id);
                    entries.put(id, e);
                }
            }
        } catch (Exception ex) { 
            throw new PersistenceException("Erro ao carregar store", ex); 
        }
    }

    // escape/unescape to avoid '|' issues (replace with percent encoding)
    private String escape(String s) {
        if (s == null)
            return "";

        return s.replace("%","%25").replace("|","%7C").replace("\n","%0A").replace("\r","%0D"); 
    }

    private String unescape(String s) {
        if (s == null) 
            return "";

        return s.replace("%0D","\r").replace("%0A","\n").replace("%7C","|").replace("%25","%");
    }
}
