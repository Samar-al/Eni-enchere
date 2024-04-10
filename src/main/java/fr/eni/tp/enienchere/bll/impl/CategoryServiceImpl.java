package fr.eni.tp.enienchere.bll.impl;

import fr.eni.tp.enienchere.bll.CategoryService;
import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.dal.CategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryDAO categoryDAO;
    @Override
    public Category getCategoryById(int categoryNb) {
        return categoryDAO.findById(categoryNb);
    }

    @Override
    public List<Category> getCategories() {
        return categoryDAO.findAll();
    }
}
