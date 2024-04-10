package fr.eni.tp.enienchere.dal.impl;

import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.dal.CategoryDAO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDAOImpl implements CategoryDAO {

    private static final String SELECT_BY_ID= "SELECT category_nb, wording FROM CATEGORY WHERE category_nb = :id";

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CategoryDAOImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    @Override
    public Category findById(int id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);
        Category category = namedParameterJdbcTemplate.queryForObject(
                SELECT_BY_ID,
                namedParameters,
                new BeanPropertyRowMapper<>(Category.class)
        );

        return category;
    }
}
