package fr.eni.tp.enienchere.dal;

import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.bo.User;

import java.util.List;

public interface BidDAO {
    public List<Bid> findAll();
    public Bid findByItemId(int itemId);
    public void create(Bid bid, User user, Long itemNb);
    public void update(Bid bid);

}
