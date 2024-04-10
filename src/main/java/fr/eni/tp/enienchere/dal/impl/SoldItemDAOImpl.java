package fr.eni.tp.enienchere.dal.impl;

import fr.eni.tp.enienchere.bll.SoldItemService;
import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.SoldItem;
import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.dal.SoldItemDAO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SoldItemDAOImpl implements SoldItemDAO {

    private static final String SELECT_BY_ID= "SELECT item_name, description, start_bid_date, end_bid_date, initial_price, sale_price, user_nb, category_nb, sales_status FROM SOLD_ITEMS WHERE item_nb=:id";
    private static final String INSERT_INTO = "INSERT INTO SOLD_ITEMS (item_name, description, start_bid_date, end_bid_date, initial_price, sale_price, user_nb, category_nb, sales_status) VALUES (:item_name, :description, :start_bid_date, :end_bid_date, :initial_price, :sale_price, :user_nb, :category_nb, :sales_status)";
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SoldItemDAOImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public SoldItem findById(int id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);
        SoldItem soldItem = namedParameterJdbcTemplate.queryForObject(
                SELECT_BY_ID,
                namedParameters,
                new BeanPropertyRowMapper<>(SoldItem.class)
        );

        return soldItem;
    }

    @Override
    public Long create(SoldItem soldItem) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("item_name", soldItem.getItemName());
        namedParameters.addValue("description", soldItem.getDescription());
        namedParameters.addValue("description", soldItem.getDescription());
        namedParameters.addValue("start_bid_date", soldItem.getDateStartBid());
        namedParameters.addValue("end_bid_date", soldItem.getDateEndBid());
        namedParameters.addValue("initial_price", soldItem.getInitialPrice());
        namedParameters.addValue("sale_price", soldItem.getSalePrice());
        namedParameters.addValue("user_nb", soldItem.getSoldUser().getUserNb());
        namedParameters.addValue("category_nb", soldItem.getCategory().getCategoryNb());
        namedParameters.addValue("sales_status", soldItem.getSaleStatus());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(INSERT_INTO, namedParameters, keyHolder);
        Number key = keyHolder.getKey();
        return key.longValue();
    }
}
