package fr.eni.tp.enienchere.bo;

import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Bid implements Serializable {
    private LocalDateTime bidDate;
    private BigDecimal bidAmount;

    private User user;

    private SoldItem soldItem;

    public LocalDateTime getBidDate() {
        return bidDate;
    }

    public Bid() {
    }

    public void setBidDate(LocalDateTime bidDate) {
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
                ", soldItem=" + soldItem +
                '}';
    }
}
