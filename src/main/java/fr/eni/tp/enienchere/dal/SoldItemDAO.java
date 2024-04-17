package fr.eni.tp.enienchere.dal;

import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.SoldItem;
import fr.eni.tp.enienchere.bo.User;

import java.util.List;

public interface SoldItemDAO {
    public SoldItem findById(int id);
    public Long create(SoldItem soldItem);
    public void update(SoldItem soldItem);
    public void delete(long itemId);
    public List<SoldItem> findAll();
    List<SoldItem>search(String filter, Integer category, long userNb, Integer openBids, Integer myCurrentBids, Integer wonBids, Integer currentSale, Integer salesNotStarted, Integer completedSales);
}
