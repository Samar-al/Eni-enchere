package fr.eni.tp.enienchere.dal;

import fr.eni.tp.enienchere.bo.User;

import java.util.List;

public interface UserDAO {
    public List<User>findAll();
    public void create(User user);
    public User read(long userId);
    User read(String username);
}
