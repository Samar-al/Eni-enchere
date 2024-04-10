package fr.eni.tp.enienchere.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CollectParcel implements Serializable {
    private String street;
    private String zipCode;
    private String city;

    private List<SoldItem> soldItems = new ArrayList<>();

    public CollectParcel() {}

    public CollectParcel(String street, String zipCode, String city) {
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<SoldItem> getSoldItems() {
        return soldItems;
    }

    public void addSoldItem(SoldItem soldItem) {
        this.soldItems.add(soldItem);
    }

    public void setSoldItems(List<SoldItem> soldItems) {
        this.soldItems = soldItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectParcel that)) return false;
        return Objects.equals(street, that.street) && Objects.equals(zipCode, that.zipCode) && Objects.equals(city, that.city) && Objects.equals(soldItems, that.soldItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, zipCode, city, soldItems);
    }

    @Override
    public String toString() {
        return "CollectParcel{" +
                "street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", soldItems=" + soldItems +
                '}';
    }
}
