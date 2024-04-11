package fr.eni.tp.enienchere.bll;

import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.SoldItem;

import java.util.List;

public interface SoldItemService {
    public SoldItem getSoldItemById(int item_nb);

    public Long create(SoldItem soldItem, String loggedUser);

    List<SoldItem>getAllSoldItems();
}
