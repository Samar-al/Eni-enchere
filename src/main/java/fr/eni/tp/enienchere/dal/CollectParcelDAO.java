package fr.eni.tp.enienchere.dal;

import fr.eni.tp.enienchere.bo.CollectParcel;

public interface CollectParcelDAO {
    public void create(CollectParcel collectParcel, Long itemNb);
    public void delete(long itemId);
}
