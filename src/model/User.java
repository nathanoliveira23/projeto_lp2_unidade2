package model;

public class User {
    private String username;
    private byte[] salt;
    private String verifierHash;

    public User(String username, byte[] salt, String verifierHash) {
        this.username = username;
        this.salt = salt;
        this.verifierHash = verifierHash;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getSalt() {
        return salt;
    }

    public String getVerifierHash() {
        return verifierHash;
    }
}
