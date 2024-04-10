package fr.eni.tp.enienchere.bll.impl;

import fr.eni.tp.enienchere.bll.SoldItemService;
import fr.eni.tp.enienchere.bo.SoldItem;
import fr.eni.tp.enienchere.dal.SoldItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SoldItemServiceImpl implements SoldItemService {

    @Autowired
    SoldItemDAO soldItemDAO;
    @Override
    public SoldItem getSoldItemById(int item_nb) {
        return soldItemDAO.findById(item_nb);
    }

    @Override
    public void create(SoldItem soldItem) {
         soldItemDAO.create(soldItem);
    }
}
