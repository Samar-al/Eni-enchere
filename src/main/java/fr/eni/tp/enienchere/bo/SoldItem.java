package fr.eni.tp.enienchere.bo;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SoldItem {
    private long itemNb;
    private String itemName;
    private String description;
    private Date dateStartBid;
    private Date dateEndBid;
    private int initialPrice;
    private int salePrice;
    private int saleStatus;

    private User soldUser;
    private User boughtUser;
    private List<Bid> bids;
    private Category category;
    private CollectParcel collectParcel;

    public SoldItem() {}

    public SoldItem(long itemNb, String itemName, String description, Date dateStartBid, Date dateEndBid, int initialPrice, int salePrice, int saleStatus) {
        this.itemNb = itemNb;
        this.itemName = itemName;
        this.description = description;
        this.dateStartBid = dateStartBid;
        this.dateEndBid = dateEndBid;
        this.initialPrice = initialPrice;
        this.salePrice = salePrice;
        this.saleStatus = saleStatus;
    }

    public long getItemNb() {
        return itemNb;
    }

    public void setItemNb(long itemNb) {
        this.itemNb = itemNb;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateStartBid() {
        return dateStartBid;
    }

    public void setDateStartBid(Date dateStartBid) {
        this.dateStartBid = dateStartBid;
    }

    public Date getDateEndBid() {
        return dateEndBid;
    }

    public void setDateEndBid(Date dateEndBid) {
        this.dateEndBid = dateEndBid;
    }

    public int getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(int initialPrice) {
        this.initialPrice = initialPrice;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public int getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(int saleStatus) {
        this.saleStatus = saleStatus;
    }

    public User getSoldUser() {
        return soldUser;
    }

    public void setSoldUser(User soldUser) {
        this.soldUser = soldUser;
    }

    public User getBoughtUser() {
        return boughtUser;
    }

    public void setBoughtUser(User boughtUser) {
        this.boughtUser = boughtUser;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void addBid(Bid bid) {
        this.bids.add(bid);
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CollectParcel getCollectParcel() {
        return collectParcel;
    }

    public void setCollectParcel(CollectParcel collectParcel) {
        this.collectParcel = collectParcel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SoldItem soldItem)) return false;
        return itemNb == soldItem.itemNb && initialPrice == soldItem.initialPrice && salePrice == soldItem.salePrice && saleStatus == soldItem.saleStatus && Objects.equals(itemName, soldItem.itemName) && Objects.equals(description, soldItem.description) && Objects.equals(dateStartBid, soldItem.dateStartBid) && Objects.equals(dateEndBid, soldItem.dateEndBid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemNb, itemName, description, dateStartBid, dateEndBid, initialPrice, salePrice, saleStatus);
    }

    @Override
    public String toString() {
        return "SoldItem{" +
                "itemNb=" + itemNb +
                ", itemName='" + itemName + '\'' +
                ", description='" + description + '\'' +
                ", dateStartBid=" + dateStartBid +
                ", dateEndBid=" + dateEndBid +
                ", initialPrice=" + initialPrice +
                ", salePrice=" + salePrice +
                ", saleStatus=" + saleStatus +
                '}';
    }
}
