package fr.eni.tp.enienchere.bll.impl;

import fr.eni.tp.enienchere.bll.BidService;
import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.dal.BidDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BidServiceImpl implements BidService {

    @Autowired
    BidDAO bidDAO;

    @Override
    public List<Bid> getAllBids() {
        return bidDAO.findAll();
    }
}
