package fr.eni.tp.enienchere.bo;

import org.springframework.security.core.userdetails.User;

import java.math.BigDecimal;
import java.util.Date;

public class Bid {
    private Date bidDate;
    private BigDecimal bidAmount;

    private User user;

    private SoldItem soldItem;

    public Date getBidDate() {
        return bidDate;
    }

    public void setBidDate(Date bidDate) {
        this.bidDate = bidDate;
    }

    public BigDecimal getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SoldItem getSoldItem() {
        return soldItem;
    }

    public void setSoldItem(SoldItem soldItem) {
        this.soldItem = soldItem;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "bidDate=" + bidDate +
                ", bidAmount=" + bidAmount +
                ", user=" + user +
                '}';
    }
}
