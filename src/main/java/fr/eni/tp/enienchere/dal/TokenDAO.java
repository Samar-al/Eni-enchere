package fr.eni.tp.enienchere.dal;

import fr.eni.tp.enienchere.bo.Token;

import java.util.List;

public interface TokenDAO {
    List<Token>findTokenByUserId(int userId);
    public void delete(long userId);

}
