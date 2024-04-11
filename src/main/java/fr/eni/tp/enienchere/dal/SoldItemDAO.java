package fr.eni.tp.enienchere.dal;

import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.SoldItem;

public interface SoldItemDAO {
    public SoldItem findById(int id);
    public Long create(SoldItem soldItem);
}
