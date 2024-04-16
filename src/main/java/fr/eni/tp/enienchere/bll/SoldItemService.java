package fr.eni.tp.enienchere.bll;

import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.SoldItem;

import java.security.Principal;
import java.util.List;

public interface SoldItemService {
    public SoldItem getSoldItemById(int item_nb);

    public Long create(SoldItem soldItem, String loggedUser);

    List<SoldItem>getAllSoldItems();

    List<SoldItem>search(String filter, Integer category);

    public void update(SoldItem soldItem);
}
