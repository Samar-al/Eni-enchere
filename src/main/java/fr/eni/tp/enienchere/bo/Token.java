package fr.eni.tp.enienchere.bo;

import java.time.LocalDateTime;

public class Token {

    private User user;
    private String token;
    private LocalDateTime expiryDate;
    private int token_nb;

    public Token(){
    }

    public Token(User user, String token, LocalDateTime expiryDate, int token_nb) {
        this.token_nb = token_nb;
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getToken_nb() {
        return token_nb;
    }

    public void setToken_nb(int token_nb) {
        this.token_nb = token_nb;
    }
}
