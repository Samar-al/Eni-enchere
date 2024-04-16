package fr.eni.tp.enienchere.bll.impl;

import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.Token;
import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.dal.UserDAO;
import fr.eni.tp.enienchere.exception.BusinessCode;
import fr.eni.tp.enienchere.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCredit(0);
        user.setAdmin(false);
        userDAO.create(user);
    }

    @Override
    public User getUser(String username) {

        return userDAO.findByUsername(username);
    }
    public void updateUser(User user) {
        userDAO.update(user);
    }

    @Override
    public void updatepassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.updatePassword(user);
    }

    @Override
    public User getUserById(long userId) {
        return userDAO.read(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDAO.read(username);
    }

    @Override
    public User getUserByEmail(String email) {
        BusinessException businessException = new BusinessException();
        User user = userDAO.findByEmail(email);
        if (!isEmailExist(user.getEmail(), businessException)) {
//            System.out.println("je passe dans la connexion");
            throw businessException;
        }

        return user;
    }

    @Override
    public void deleteUser(User user) {
        userDAO.delete(user);
    }

    @Override
    public void createTokenUser(Token token) {
        userDAO.createTokenUser(token);
    }

    @Override
    public Token getTokenUser(String token) {
        return userDAO.findToken(token);
    }

    private boolean isEmailExist(
            String email,
            BusinessException businessException
    ) {
        if(email.equals("DONOTEXIST")) {
            businessException.add(BusinessCode.EMAIL_NOT_EXIST);
            return false;
        }
        return true;
    }
}
