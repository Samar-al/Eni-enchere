package fr.eni.tp.enienchere.bll;

public interface EmailService {

    void sendPasswordResetEmail(String to, String token);
}
