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



import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
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
        newBid.setBidDate(LocalDateTime.now());

        // Retrieve existing bid for the user and item
        Bid existingBid = bidDAO.getBidByItemNumber(itemNumber);

        if(!isBidClosed(soldItemDAO.findById(itemNumber).getSaleStatus(), businessException)) {
            throw businessException;
        }

        if(!isBidderSeller(user, soldItemDAO.findById(itemNumber).getSoldUser(), businessException )) {
            throw businessException;
        }

        // Check if the user has enough credit
        if (!isCreditEnough(userCredit, newBid.getBidAmount(), businessException)) {
            throw businessException;
        }

        // Check if the new bid amount is greater than the existing bid amount
        if (existingBid.getUser() != null && !isBidAmountEnough(newBid.getBidAmount(), existingBid.getBidAmount(), businessException)) {
            throw businessException;
        }

        // Update the user's credit and the bid
        if (existingBid.getUser() == null) {
            // No existing bid, insert the new bid
            BigDecimal newUserCredit = userCredit.subtract(newBid.getBidAmount());
            user.setCredit(newUserCredit.longValue());
            userDAO.update(user);
            bidDAO.insertBid(newBid, user.getUserNb(), itemNumber);
        } else {
            // Existing bid found, update with new bid amount and date
            System.out.println(existingBid);
            User previousUser = userDAO.findByUsername(existingBid.getUser().getUsername());
            if (previousUser.equals(user)) {
                // The new bid is made by the same user as the previous bid
                userCredit = userCredit.add(existingBid.getBidAmount());
            }
            BigDecimal previousUserCurrentCreditDecimal = BigDecimal.valueOf(previousUser.getCredit());
            BigDecimal previousUserNewCreditDecimal = previousUserCurrentCreditDecimal.add(existingBid.getBidAmount());
            previousUser.setCredit(previousUserNewCreditDecimal.longValue());
            userDAO.update(previousUser);

            BigDecimal newUserCredit = userCredit.subtract(newBid.getBidAmount());
            user.setCredit(newUserCredit.longValue());
            userDAO.update(user);

            existingBid.setBidAmount(newBid.getBidAmount());
            existingBid.setBidDate(newBid.getBidDate());
            bidDAO.updateBid(existingBid, user.getUserNb());
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

    private boolean isBidClosed(
            int salesStatus,
            BusinessException businessException
    ) {
        if(salesStatus == 2){
            businessException.add(BusinessCode.BID_HAS_ENDED);
            return false;
        }
        return true;
    }
    private boolean isBidAmountEnough(
            BigDecimal newBidAmount,
            BigDecimal bidAmount,
            BusinessException businessException
    ) {
        if(newBidAmount.compareTo(bidAmount) <= 0){
            businessException.add(BusinessCode.NOT_ENOUGH_BID_AMOUNT);
            return false;
        }
        return true;
    }

    private boolean isBidderSeller(
            User bidder,
            User seller,
            BusinessException businessException
    ) {
        if(bidder == seller){
            businessException.add(BusinessCode.NOT_AUTORISED_BID);
            return false;
        }
        return true;
    }

}
