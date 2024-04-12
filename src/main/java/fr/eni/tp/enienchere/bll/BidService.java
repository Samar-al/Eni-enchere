package fr.eni.tp.enienchere.bll;

import fr.eni.tp.enienchere.bo.Bid;

import java.util.ArrayList;
import java.util.List;

public interface BidService {
    List<Bid>getAllBids();

    public void placeBid(Bid newBid, String loggedUsername, String itemNb);
}
