package fr.eni.tp.enienchere.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {

    private long categoryNb;
    private String wording;

    List<SoldItem> soldItemsPerCategory = new ArrayList<>();

    public Category() {
    }
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

    @Override
    public String toString() {
        return "Category{" +
                "categoryNb=" + categoryNb +
                ", wording='" + wording + '\'' +
                ", soldItemsPerCategory=" + soldItemsPerCategory +
                '}';
    }
}
