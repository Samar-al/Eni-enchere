package fr.eni.tp.enienchere.bll;

import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.SoldItem;
import fr.eni.tp.enienchere.bo.User;

import java.security.Principal;
import java.util.List;

public interface SoldItemService {
    public SoldItem getSoldItemById(int item_nb);

    public Long create(SoldItem soldItem, String loggedUser);

    List<SoldItem>getAllSoldItems();

    public void update(SoldItem soldItem);

    List<SoldItem>search(String filter, Integer category, long userNb, Integer openBids, Integer myCurrentBids, Integer wonBids, Integer currentSale, Integer salesNotStarted, Integer completedSales);

    void delete(SoldItem soldItem);
}
