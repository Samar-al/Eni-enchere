package fr.eni.tp.enienchere.dal.impl;

import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.SoldItem;
import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.dal.BidDAO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public class BidDAOImpl implements BidDAO {

    private static final String SELECT_ALL = "SELECT b.bid_date, b.bid_amount, s.user_nb, s.item_nb, s.item_name, s.description, s.start_bid_date, s.end_bid_date, s.initial_price, s.sales_status, c.wording, u.user_nb, u.username FROM BIDS as b LEFT JOIN SOLD_ITEMS as s ON b.item_nb = s.item_nb LEFT JOIN  CATEGORY AS c ON s.category_nb = c.category_nb LEFT JOIN USERS as u ON s.user_nb = u.user_nb";
    private static final String INSERT_INTO = "INSERT INTO BIDS (user_nb, item_nb, bid_date, bid_amount) VALUES (:user_nb, :item_nb, :bid_date, :bid_amount)";
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BidDAOImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    @Override
    public List<Bid> findAll() {
        List<Bid>bids = jdbcTemplate.query(SELECT_ALL, new BidRowMapper());
        return bids;
    }

    @Override
    public void create(Bid bid, User user, Long itemNb) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("user_nb", user.getUserNb());
        namedParameters.addValue("item_nb", itemNb);
        namedParameters.addValue("bid_date", bid.getBidDate());
        namedParameters.addValue("bid_amount", bid.getBidAmount());

        namedParameterJdbcTemplate.update(INSERT_INTO, namedParameters);


    }

    @Override
    public void update(Bid bid) {


    }


    public class BidRowMapper implements RowMapper<Bid> {

        @Override
        public Bid mapRow(ResultSet rs, int rowNum) throws SQLException {

            Bid bid = new Bid();
            bid.setBidDate(toLocalDateTime(rs.getTimestamp("b.bid_date")));
            bid.setBidAmount(rs.getBigDecimal("b.bid_amount"));

            SoldItem soldItem = new SoldItem();
            soldItem.setItemNb(rs.getInt("s.item_nb"));
            soldItem.setItemName(rs.getString("s.item_name"));
            soldItem.setDescription(rs.getString("s.description"));
            soldItem.setDateStartBid(rs.getDate("s.start_bid_date"));
            soldItem.setDateEndBid(rs.getDate("s.end_bid_date"));
            soldItem.setInitialPrice(rs.getInt("s.initial_price"));
            soldItem.setInitialPrice(rs.getInt("s.sales_status"));


            Category category = new Category();
            category.setWording(rs.getString("c.wording"));
            soldItem.setCategory(category);


            User user = new User();
            user.setUserNb(rs.getInt("s.user_nb"));
            user.setUsername(rs.getString("u.username"));
            soldItem.setSoldUser(user);
            bid.setSoldItem(soldItem);
            return bid;


        }

        private LocalDateTime toLocalDateTime(java.sql.Timestamp timestamp) {
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        }
    }
}
