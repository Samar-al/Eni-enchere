package fr.eni.tp.enienchere.controller.converter;

import fr.eni.tp.enienchere.bll.CategoryService;
import fr.eni.tp.enienchere.bo.Category;
import org.springframework.core.convert.converter.Converter;

public class StringToCategoryConverter implements Converter<String, Category> {
    private CategoryService categoryService;

    public StringToCategoryConverter(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Override
    public Category convert(String id) {
        int categoryNb = Integer.parseInt(id);
        return categoryService.getCategoryById(categoryNb);
    }
}
