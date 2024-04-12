package fr.eni.tp.enienchere.bll.impl;

import fr.eni.tp.enienchere.bll.BidService;
import fr.eni.tp.enienchere.bo.Bid;



import fr.eni.tp.enienchere.bo.User;

import fr.eni.tp.enienchere.dal.BidDAO;
import fr.eni.tp.enienchere.dal.CollectParcelDAO;
import fr.eni.tp.enienchere.dal.SoldItemDAO;
import fr.eni.tp.enienchere.dal.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import fr.eni.tp.enienchere.exception.BusinessCode;
import fr.eni.tp.enienchere.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class BidServiceImpl implements BidService {

    @Autowired
    BidDAO bidDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    SoldItemDAO soldItemDAO;

    @Autowired
    CollectParcelDAO collectParcelDAO;

    @Override
    public List<Bid> getAllBids() {
        return bidDAO.findAll();
    }


    @Override
    public Bid getBidByItemId(int itemId) {
        Bid bid = bidDAO.getBidByItemNumber(itemId);
        if(bid == null) {
            Bid emptyBid = new Bid();
            emptyBid.setBidAmount(BigDecimal.valueOf(0));
            return emptyBid;
        }

        return bid;
    }

    @Override
    public void placeBid(Bid newBid, String loggedUser, String itemNb) {
        BusinessException businessException = new BusinessException();
        User user = userDAO.findByUsername(loggedUser);
        BigDecimal userCredit = BigDecimal.valueOf(user.getCredit());
        int itemNumber = Integer.parseInt(itemNb);
        // Retrieve existing bid for the user and item
        Bid existingBid = bidDAO.getBidByItemNumber(itemNumber);
        boolean isCreditValid = isCreditEnough(userCredit, newBid.getBidAmount(), businessException);
            // New bid is higher or there's no existing bid, so update or insert
        if (existingBid == null ) {
            if(isCreditValid) {
                // No existing bid, insert the new bid
                BigDecimal newUserCredit = userCredit.subtract(newBid.getBidAmount());
                Long newUserCreditLong = newUserCredit.longValue();
                user.setCredit(newUserCreditLong);
                bidDAO.insertBid(newBid, user.getUserNb(), itemNumber);
            }

        } else {

            if(newBid.getBidAmount().compareTo(existingBid.getBidAmount()) > 0 && isCreditValid) {
                // Existing bid found, update with new bid amount and date
                User previousUser = userDAO.findByUsername(existingBid.getUser().getUsername());
                BigDecimal previousUserCurrentCreditDecimal = BigDecimal.valueOf(previousUser.getCredit());
                BigDecimal previousUserNewCreditDecimal = previousUserCurrentCreditDecimal.add(existingBid.getBidAmount());
                Long previousUserNewCreditLong = previousUserNewCreditDecimal.longValue();
                previousUser.setCredit(previousUserNewCreditLong);
                userDAO.update(previousUser);
                existingBid.setBidAmount(newBid.getBidAmount());
                existingBid.setBidDate(newBid.getBidDate());
                bidDAO.updateBid(existingBid, user.getUserNb());
            }
        }

    }

    private boolean isCreditEnough(
            BigDecimal userCredit,
            BigDecimal bidAmount,
            BusinessException businessException
    ) {
        if (userCredit.compareTo(bidAmount) < 0) {
            businessException.add(BusinessCode.NOT_ENOUGH_CREDIT);
            return false;
        }
        return true;
    }

}
