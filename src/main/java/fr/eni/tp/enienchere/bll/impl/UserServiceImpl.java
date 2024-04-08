package fr.eni.tp.enienchere.bll.impl;

import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.dal.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDAO userDAO;
    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
}
