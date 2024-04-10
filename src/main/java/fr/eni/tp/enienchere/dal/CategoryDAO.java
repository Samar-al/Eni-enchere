package fr.eni.tp.enienchere.dal;

import fr.eni.tp.enienchere.bo.Category;

import java.util.List;

public interface CategoryDAO {
    public Category findById(int id);
    List<Category> findAll();
}
