package model;

import java.util.UUID;

public class VaultEntry {
    private String id;
    private String title;
    private String username;
    private String encryptedPassword;
    private String url;
    private String notes;

    public VaultEntry(String title, String username, String encryptedPassword, String url, String notes) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.url = url;
        this.notes = notes;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getUsername() { return username; }
    public String getEncryptedPassword() { return encryptedPassword; }
    public String getUrl() { return url; }
    public String getNotes() { return notes; }
    public void setTitle(String title) { this.title = title; }
    public void setUsername(String username) { this.username = username; }
    public void setEncryptedPassword(String encryptedPassword) { this.encryptedPassword = encryptedPassword; }
    public void setUrl(String url) { this.url = url; }
    public void setNotes(String notes) { this.notes = notes; }
    @Override public String toString() { return "[" + id + "] " + title + " (" + username + ") - " + url; }
}
