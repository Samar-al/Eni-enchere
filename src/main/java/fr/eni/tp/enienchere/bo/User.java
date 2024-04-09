package fr.eni.tp.enienchere.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private long userNb;
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String username;
    @NotEmpty
    private String lastname;
    @NotBlank
    private String firstname;
    @NotBlank
    private String email;
    @NotBlank
    private String phone;
    private String street;
    private String zipCode;
    private String city;
    @NotBlank
    private String password;
    private long credit = 0;
    private boolean admin = false;

    List<Bid> bids = new ArrayList<>();

    List<SoldItem> soldItems = new ArrayList<>();

    List<SoldItem> boughtItems =  new ArrayList<>();

    public User() {
    }

    public User(String username, String lastname, String firstname, String email, String password) {
        this.username = username;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.password = password;
    }

    public long getUserNb() {
        return userNb;
    }

    public void setUserNb(long userNb) {
        this.userNb = userNb;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCredit() {
        return credit;
    }

    public void setCredit(long credit) {
        this.credit = credit;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void addBid(Bid bid) {
        this.bids.add(bid);
    }

    public List<SoldItem> getSoldItems() {
        return soldItems;
    }

    public void addSoldItem(SoldItem soldItem) {
        this.soldItems.add(soldItem);
    }

    public List<SoldItem> getBoughtItems() {
        return boughtItems;
    }

    public void addBoughtItem(SoldItem boughtItem) {
        this.boughtItems.add(boughtItem);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "userNb=" + userNb +
                ", username='" + username + '\'' +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", street='" + street + '\'' +
                ", zipCode=" + zipCode +
                ", credit=" + credit +
                ", admin=" + admin +
                '}';
    }
}
