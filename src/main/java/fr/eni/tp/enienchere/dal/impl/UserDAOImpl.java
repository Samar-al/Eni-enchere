package fr.eni.tp.enienchere.dal.impl;

import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.dal.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class UserDAOImpl implements UserDAO {

    private static final String SELECT_ALL = "SELECT u.lastname, u.firstname FROM USERS as u";
    private static final String INSERT_USER = "INSERT INTO USERS (username, lastname, firstname, email, phone, street, zip_code, city, password, credit, admin) \n" +
            "VALUES (:username, :lastname, :firstname, :email, :phone, :street, :zip_code, :city, :password, :credit, :admin);";
    private static final String SELECT_BY_ID = "SELECT username, lastname, firstname, email, phone, street, zip_code, city FROM users WHERE user_nb = :userId;";
    private static final String SELECT_BY_USERNAME = "SELECT user_nb, username, lastname, firstname, email, phone, street, zip_code, city FROM users WHERE username = :username;";

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

    @Override
    public void create(User user) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("username", user.getUsername());
        namedParameters.addValue("lastname", user.getLastname());
        namedParameters.addValue("firstname", user.getFirstname());
        namedParameters.addValue("email", user.getEmail());
        namedParameters.addValue("phone", user.getPhone());
        namedParameters.addValue("street", user.getStreet());
        namedParameters.addValue("zip_code", user.getZipCode());
        namedParameters.addValue("city", user.getCity());
        namedParameters.addValue("password", user.getPassword());
        namedParameters.addValue("credit", user.getCredit());
        namedParameters.addValue("admin", user.isAdmin());

        namedParameterJdbcTemplate.update(INSERT_USER,namedParameters);
    }

    @Override
    public User read(long userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        User user = namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, namedParameters, new BeanPropertyRowMapper<>(User.class));
        return user;
    }

    @Override
    public User read(String username) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("username", username);
        User user = namedParameterJdbcTemplate.queryForObject(SELECT_BY_USERNAME, namedParameters, new BeanPropertyRowMapper<>(User.class));
        return user;
    }
}
