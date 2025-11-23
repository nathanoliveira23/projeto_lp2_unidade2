package repository;

import java.util.List;

import model.User;

public interface IUserRepository {

    void addUser(User user) throws Exception;
    List<User> findAll() throws Exception;
    User findUserByName(String username) throws Exception;
    void deleteUser(String username) throws Exception;
}
