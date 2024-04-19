package fr.eni.tp.enienchere.bll.impl;

import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.bo.SoldItem;
import fr.eni.tp.enienchere.bo.Token;
import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.dal.BidDAO;
import fr.eni.tp.enienchere.dal.SoldItemDAO;
import fr.eni.tp.enienchere.dal.TokenDAO;
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
    BidDAO bidDAO;

    @Autowired
    SoldItemDAO soldItemDAO;

    @Autowired
    TokenDAO tokenDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;
    final static int USER_CREDIT_AT_START = 0;
    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCredit(USER_CREDIT_AT_START);
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
        List<Bid> bidsOfUser = bidDAO.findAllBidsByUserId((int) user.getUserNb());
        User userSupprime = userDAO.findByUsername("Utilisateur supprimé");
        for (Bid bid : bidsOfUser) {
            bidDAO.updateBid(bid, userSupprime.getUserNb());
        }
        List<SoldItem>soldItemsofUser = soldItemDAO.findAllByUserId((int)user.getUserNb());
        for (SoldItem soldItem : soldItemsofUser) {

            if(soldItem.getSoldUser().getUserNb() == user.getUserNb()) {
                soldItem.setSoldUser(userSupprime);
            }
            if(soldItem.getBoughtUser() != null && soldItem.getBoughtUser().getUserNb() == user.getUserNb()) {
                soldItem.setBoughtUser(userSupprime);
            }

            soldItemDAO.update(soldItem);
        }

        List<Token>tokens = tokenDAO.findTokenByUserId((int)user.getUserNb());

        for (Token token : tokens) {

            tokenDAO.delete(user.getUserNb());
        }

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
