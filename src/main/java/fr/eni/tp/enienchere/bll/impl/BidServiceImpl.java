package fr.eni.tp.enienchere.bll.impl;

import fr.eni.tp.enienchere.bll.BidService;
import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.bo.SoldItem;
import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.dal.BidDAO;
import fr.eni.tp.enienchere.dal.CollectParcelDAO;
import fr.eni.tp.enienchere.dal.SoldItemDAO;
import fr.eni.tp.enienchere.dal.UserDAO;
import fr.eni.tp.enienchere.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    public void placeBid(Bid newBid, String loggedUser, String itemNb) {
        User user = userDAO.findByUsername(loggedUser);
        int itemNumber = Integer.parseInt(itemNb);
        // Retrieve existing bid for the user and item
        Bid existingBid = bidDAO.getBidByItemNumber(itemNumber);

        if (existingBid == null || newBid.getBidAmount().compareTo(existingBid.getBidAmount()) > 0) {
            // New bid is higher or there's no existing bid, so update or insert
            if (existingBid == null) {
                // No existing bid, insert the new bid
                bidDAO.insertBid(newBid, user.getUserNb(), itemNumber);
            } else {
                // Existing bid found, update with new bid amount and date
                existingBid.setBidAmount(newBid.getBidAmount());
                existingBid.setBidDate(newBid.getBidDate());
                bidDAO.updateBid(existingBid, user.getUserNb());
            }
        }
    }

}
