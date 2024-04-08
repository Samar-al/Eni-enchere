package fr.eni.tp.enienchere.dal.impl;

import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.dal.UserDAO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class UserDAOImpl implements UserDAO {

    private static final String SELECT_ALL = "SELECT u.lastname, u.firstname FROM USERS as u";

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDAOImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    @Override
    public List<User> findAll() {
        List<User>users = jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(User.class));
        return users;
    }
}
