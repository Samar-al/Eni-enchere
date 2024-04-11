package fr.eni.tp.enienchere.bll;

import fr.eni.tp.enienchere.bo.Category;

import java.util.List;

public interface CategoryService {
    public Category getCategoryById(int categoryNb);

    List<Category> getAllCategory();

    List<Category> getCategories();


}
