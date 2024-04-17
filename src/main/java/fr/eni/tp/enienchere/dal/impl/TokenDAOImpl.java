package fr.eni.tp.enienchere.dal.impl;

import fr.eni.tp.enienchere.bo.SoldItem;
import fr.eni.tp.enienchere.bo.Token;
import fr.eni.tp.enienchere.dal.TokenDAO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class TokenDAOImpl implements TokenDAO {
    private static final String SELECT_ALL_BY_USER_ID = "SELECT token_nb, user_nb, token, expiryDate FROM token WHERE user_nb = :userId";
    private static final String DELETE = "DELETE FROM token WHERE user_nb = :userId";
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TokenDAOImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    @Override
    public List<Token> findTokenByUserId(int userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        List<Token>tokens = namedParameterJdbcTemplate.query(SELECT_ALL_BY_USER_ID, namedParameters, new BeanPropertyRowMapper<>(Token.class));
        return tokens;
    }

    @Override
    public void delete(long userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        namedParameterJdbcTemplate.update(DELETE, namedParameters);
    }
}
