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

      //  bidDAO.create(bid, user, newItemId);



    }

   /* @Override
    public void updateBid(Bid bid, Long loggedUser) {
        return bidDAO.update(bid);
    }
*/

}
