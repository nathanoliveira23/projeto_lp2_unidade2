package service;

import java.util.List;

import model.VaultEntry;

public interface IVaultService {

    void register(String username, char[] masterPassword) throws Exception;
    void login(String username, char[] masterPassword) throws Exception;
    void updateUsername(String newUsername) throws Exception; 
    void updatePassword(char[] newPassword) throws Exception;
    void deleteAccount() throws Exception;

    void addEntry(String title, String UsernameEntry, String PlainPassword, String url, String notes) throws Exception;
    List<VaultEntry> listEntries();
    void updateEntry(String id, String title, String usernameEntry, String plainPassword, String url, String notes) throws Exception;
    void removeEntry(String id) throws Exception;

    String generatePassword(int length);
    String generatePassword(String text);
}
