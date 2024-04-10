package fr.eni.tp.enienchere.bll;

import fr.eni.tp.enienchere.bo.User;

import java.util.List;

public interface UserService {
    List<User>getAllUsers();
    public void addUser(User user);
    User getUserById(long userId);
    User getUserByUsername(String username);
}
