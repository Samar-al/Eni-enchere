package fr.eni.tp.enienchere.dal;

import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.SoldItem;

import java.util.List;

public interface SoldItemDAO {
    public SoldItem findById(int id);
    public Long create(SoldItem soldItem);

    public List<SoldItem> findAll();

}
