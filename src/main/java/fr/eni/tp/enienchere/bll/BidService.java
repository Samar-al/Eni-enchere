package fr.eni.tp.enienchere.bll;

import fr.eni.tp.enienchere.bo.Bid;

import java.util.ArrayList;
import java.util.List;

public interface BidService {
    List<Bid>getAllBids();

    public void createBid(Bid bid, String loggedUser);
   /* public void updateBid(Bid bid, Long loggedUser);*/
}
