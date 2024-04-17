package fr.eni.tp.enienchere.bll.impl;

import fr.eni.tp.enienchere.bll.SoldItemService;
import fr.eni.tp.enienchere.bll.UserService;
import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.bo.SoldItem;
import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.dal.BidDAO;
import fr.eni.tp.enienchere.dal.CollectParcelDAO;
import fr.eni.tp.enienchere.dal.SoldItemDAO;
import fr.eni.tp.enienchere.dal.UserDAO;
import fr.eni.tp.enienchere.exception.BusinessCode;
import fr.eni.tp.enienchere.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class SoldItemServiceImpl implements SoldItemService {

    @Autowired
    SoldItemDAO soldItemDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    CollectParcelDAO collectParcelDAO;

    @Autowired
    BidDAO bidDAO;

    final static int SALE_STATUS_NOT_STARTED = 0;
    final static int SALE_STATUS_IN_PROGRESS = 1;
    final static int SALE_STATUS_CLOSED = 2;
    final static int NO_CATEGORY = -1;


    @Override
    public SoldItem getSoldItemById(int item_nb) {
        SoldItem soldItem = soldItemDAO.findById(item_nb);
        return soldItem;
    }

    @Override
    public Long create(SoldItem soldItem, String loggedUser) {
        BusinessException businessException = new BusinessException();
        User user = userDAO.findByUsername(loggedUser);
        soldItem.setSoldUser(user);
        Date today = new Date();
        if (today.before(soldItem.getDateStartBid()))
        {
            soldItem.setSaleStatus(SALE_STATUS_NOT_STARTED);
        } else if ((soldItem.getDateStartBid().equals(today) || soldItem.getDateStartBid().after(today)) &&
                soldItem.getDateEndBid().equals(today) || soldItem.getDateEndBid().after(today)
        ) {
            soldItem.setSaleStatus(SALE_STATUS_IN_PROGRESS);
        } else if (soldItem.getDateEndBid().before(today))
        {
            soldItem.setSaleStatus(SALE_STATUS_CLOSED);
        }

        if ((soldItem.getCollectParcel().getStreet() == null || soldItem.getCollectParcel().getStreet().isEmpty()) ||
                (soldItem.getCollectParcel().getZipCode() == null || soldItem.getCollectParcel().getZipCode().isEmpty()) ||
                (soldItem.getCollectParcel().getCity() == null || soldItem.getCollectParcel().getCity().isEmpty())
        ) {

            soldItem.getCollectParcel().setStreet(user.getStreet());
            soldItem.getCollectParcel().setZipCode(user.getZipCode());
            soldItem.getCollectParcel().setCity(user.getCity());
        }

        Long newItemId = soldItemDAO.create(soldItem);
        collectParcelDAO.create(soldItem.getCollectParcel(), newItemId);
        return newItemId;
    }

    @Override
    public List<SoldItem> getAllSoldItems() {
        return soldItemDAO.findAll();
    }

    @Override
    public List<SoldItem> search(String filter, Integer category, long userNb, Integer openBids, Integer myCurrentBids, Integer wonBids, Integer currentSale, Integer salesNotStarted, Integer completedSales) {
        if (filter != null) {
            filter = filter.toLowerCase();
        }

        // If category is -1, set it to null to indicate no category filter
        if (category != null && category == NO_CATEGORY) {
            category = null;
        }

        return soldItemDAO.search(filter, category, userNb, openBids, myCurrentBids, wonBids, currentSale, salesNotStarted, completedSales);
    }

    @Override
    public void delete(SoldItem soldItem) {
        if(soldItem.getSaleStatus()==0) {
            collectParcelDAO.delete(soldItem.getItemNb());
            soldItemDAO.delete(soldItem.getItemNb());
        }else{
            BusinessException businessException = new BusinessException();
            businessException.add(BusinessCode.BID_HAS_ALREADY_STARTED);
            throw businessException;
        }
    }

    @Override
    public void update(SoldItem soldItem) {
        if(soldItem.getSaleStatus() == SALE_STATUS_NOT_STARTED) {
            soldItemDAO.update(soldItem);
        }else {
            BusinessException businessException = new BusinessException();
            businessException.add(BusinessCode.BID_HAS_ALREADY_STARTED);
            throw businessException;
        }

    }
}
