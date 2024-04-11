package fr.eni.tp.enienchere.bll.impl;

import fr.eni.tp.enienchere.bll.SoldItemService;
import fr.eni.tp.enienchere.bo.SoldItem;
import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.dal.CollectParcelDAO;
import fr.eni.tp.enienchere.dal.SoldItemDAO;
import fr.eni.tp.enienchere.dal.UserDAO;
import fr.eni.tp.enienchere.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Override
    public SoldItem getSoldItemById(int item_nb) {
        return soldItemDAO.findById(item_nb);
    }

    @Override
    public void create(SoldItem soldItem, String loggedUser) {
        BusinessException businessException = new BusinessException();
        User user = userDAO.findByUsername(loggedUser);
        soldItem.setSoldUser(user);
        Date today = new Date();
        if(soldItem.getDateStartBid().before(today)) {
            soldItem.setSaleStatus(0);
        }
        if((soldItem.getDateStartBid().equals(today) || soldItem.getDateStartBid().after(today)) &&
                soldItem.getDateEndBid().equals(today) || soldItem.getDateEndBid().after(today)
        ) {
            soldItem.setSaleStatus(1);
        }
        if(soldItem.getDateEndBid().before(today)){
            soldItem.setSaleStatus(2);
        }
        if((soldItem.getCollectParcel().getStreet() == null || soldItem.getCollectParcel().getStreet().isEmpty()) ||
                (soldItem.getCollectParcel().getZipCode() == null || soldItem.getCollectParcel().getZipCode().isEmpty()) ||
                (soldItem.getCollectParcel().getCity() == null || soldItem.getCollectParcel().getCity().isEmpty())
        ) {
            soldItem.getCollectParcel().setStreet(user.getStreet());
            soldItem.getCollectParcel().setZipCode(user.getZipCode());
            soldItem.getCollectParcel().setCity(user.getCity());
        }

        Long newItemId = soldItemDAO.create(soldItem);
        collectParcelDAO.create(soldItem.getCollectParcel(), newItemId);
    }

    @Override
    public List<SoldItem> getAllSoldItems() {
        return soldItemDAO.findAll();
    }
}
