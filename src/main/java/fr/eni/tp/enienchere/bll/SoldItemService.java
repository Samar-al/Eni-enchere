package fr.eni.tp.enienchere.bll;

import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.SoldItem;

public interface SoldItemService {
    public SoldItem getSoldItemById(int item_nb);

    public void create(SoldItem soldItem);
}
