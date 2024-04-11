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

    @Override
    public void createBid(Bid bid, String loggedUser) {
        BusinessException businessException = new BusinessException();
        User user = userDAO.findByUsername(loggedUser);
        BigDecimal bidAmount = new BigDecimal(bid.getSoldItem().getInitialPrice());
        bid.setBidAmount(bidAmount);
        bid.setBidDate(LocalDateTime.now());
        bid.getSoldItem().setSoldUser(user);
        Date today = new Date();
        if(bid.getSoldItem().getDateStartBid().before(today)) {
            bid.getSoldItem().setSaleStatus(0);
        }
        if((bid.getSoldItem().getDateStartBid().equals(today) || bid.getSoldItem().getDateStartBid().after(today)) &&
                bid.getSoldItem().getDateEndBid().equals(today) || bid.getSoldItem().getDateEndBid().after(today)
        ) {
            bid.getSoldItem().setSaleStatus(1);
        }
        if(bid.getSoldItem().getDateEndBid().before(today)){
            bid.getSoldItem().setSaleStatus(2);
        }
        if((bid.getSoldItem().getCollectParcel().getStreet() == null || bid.getSoldItem().getCollectParcel().getStreet().isEmpty()) ||
                (bid.getSoldItem().getCollectParcel().getZipCode() == null || bid.getSoldItem().getCollectParcel().getZipCode().isEmpty()) ||
                (bid.getSoldItem().getCollectParcel().getCity() == null || bid.getSoldItem().getCollectParcel().getCity().isEmpty())
        ) {
            bid.getSoldItem().getCollectParcel().setStreet(user.getStreet());
            bid.getSoldItem().getCollectParcel().setZipCode(user.getZipCode());
            bid.getSoldItem().getCollectParcel().setCity(user.getCity());
        }

        Long newItemId = soldItemDAO.create(bid.getSoldItem());
        collectParcelDAO.create(bid.getSoldItem().getCollectParcel(), newItemId);
        bidDAO.create(bid, user, newItemId);



    }

   /* @Override
    public void updateBid(Bid bid, Long loggedUser) {
        return bidDAO.update(bid);
    }
*/

}
