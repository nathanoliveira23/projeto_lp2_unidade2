package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import model.User;
import util.CSVUtil;

public class UserRepository {
    private static final Path USERS = Paths.get("data/users.csv");

    public UserRepository() throws IOException {
        Files.createDirectories(USERS.getParent());

        if (!Files.exists(USERS))
            Files.createFile(USERS);
    }

    public void addUser(User user) throws IOException {
        String[] newUser = {
            user.getUsername(),
            Base64.getEncoder().encodeToString(user.getSalt()),
            user.getVerifierHash()
        };

        CSVUtil.appendCsv(USERS, newUser);
    }

    public List<User> findAll() throws IOException {
        List<String[]> rows = CSVUtil.readCSV(USERS);
        List<User> users = new ArrayList<>();

        for (String[] p : rows) {
            if (p.length < 3) continue;

            byte[] salt = Base64.getDecoder().decode(p[1]);
            String hash = p[2];
            User user = new User(p[0], salt, hash);

            users.add(user);
        }

        return users;
    }

    public User findUserByName(String username) throws IOException {
        for (User user : findAll()) {
            if (user.getUsername().equals(username)) 
                return user;
        }

        return null;
    }

    public void updateAll(List<User> users) throws IOException {
        List<String[]> rows = new ArrayList<>();

        for (User u : users) {
            rows.add(new String[]{
                u.getUsername(),
                Base64.getEncoder().encodeToString(u.getSalt()),
                u.getVerifierHash()
            });
        }

        CSVUtil.writeCsv(USERS, rows);
    }

    public void deleteUser(String username) throws IOException {
        List<User> users = findAll();
        users.removeIf(u -> u.getUsername().equals(username));
        updateAll(users);
    }
}
