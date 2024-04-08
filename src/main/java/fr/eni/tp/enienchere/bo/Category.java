package fr.eni.tp.enienchere.bo;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private long categoryNb;
    private String wording;

    List<SoldItem> soldItemsPerCategory = new ArrayList<>();

    public Category(String wording) {
        this.wording = wording;
    }

    public long getCategoryNb() {
        return categoryNb;
    }

    public void setCategoryNb(long categoryNb) {
        this.categoryNb = categoryNb;
    }

    public String getWording() {
        return wording;
    }

    public void setWording(String wording) {
        this.wording = wording;
    }

    public List<SoldItem> getSoldItems() {
        return soldItemsPerCategory;
    }

    public void addSoldItem(SoldItem soldItem) {
        this.soldItemsPerCategory.add(soldItem);
    }
}
