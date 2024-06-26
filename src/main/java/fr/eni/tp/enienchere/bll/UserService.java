package fr.eni.tp.enienchere.bll;

import fr.eni.tp.enienchere.bo.Token;
import fr.eni.tp.enienchere.bo.User;

import java.util.List;

public interface UserService {
    List<User>getAllUsers();
    public void addUser(User user);


    public User getUser(String username);

    User getUserById(long userId);
    User getUserByUsername(String username);
    User getUserByEmail(String email);

    void updateUser(User user);
    void updatepassword(User user);
    void deleteUser(User user);

    void createTokenUser(Token token);
    Token getTokenUser(String token);

}
