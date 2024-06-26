package fr.eni.tp.enienchere.dal.impl;

import fr.eni.tp.enienchere.bo.Bid;
import fr.eni.tp.enienchere.bo.Category;
import fr.eni.tp.enienchere.bo.SoldItem;
import fr.eni.tp.enienchere.bo.User;
import fr.eni.tp.enienchere.dal.BidDAO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
@Repository

public class BidDAOImpl implements BidDAO {

    private static final String SELECT_ALL = "SELECT b.bid_date, b.bid_amount, b.user_nb, s.item_nb, s.item_name, s.description, s.start_bid_date, s.end_bid_date, s.initial_price, s.sales_status, c.wording, u.user_nb, u.username FROM BIDS as b LEFT JOIN SOLD_ITEMS as s ON b.item_nb = s.item_nb LEFT JOIN  CATEGORY AS c ON s.category_nb = c.category_nb LEFT JOIN USERS as u ON b.user_nb = u.user_nb";
    private static final String SELECT_BY_ITEM_ID = "SELECT b.bid_date, b.bid_amount, b.user_nb, s.user_nb, s.item_nb, s.item_name, s.description, s.start_bid_date, s.end_bid_date, s.initial_price, s.sales_status, c.wording, u.user_nb, u.username, u.lastname FROM BIDS as b LEFT JOIN SOLD_ITEMS as s ON b.item_nb = s.item_nb LEFT JOIN  CATEGORY AS c ON s.category_nb = c.category_nb LEFT JOIN USERS as u ON b.user_nb = u.user_nb WHERE s.item_nb=:item_nb";
    private static final String SELECT_ALL_BY_USER_ID = "SELECT b.bid_date, b.bid_amount, b.user_nb, s.user_nb, s.item_nb, s.item_name, s.description, s.start_bid_date, s.end_bid_date, s.initial_price, s.sales_status, c.wording, u.user_nb, u.username, u.lastname FROM BIDS as b LEFT JOIN SOLD_ITEMS as s ON b.item_nb = s.item_nb LEFT JOIN  CATEGORY AS c ON s.category_nb = c.category_nb LEFT JOIN USERS as u ON b.user_nb = u.user_nb WHERE b.user_nb = :user_nb";
    private static final String INSERT_INTO = "INSERT INTO BIDS (user_nb, item_nb, bid_date, bid_amount) VALUES (:user_nb, :item_nb, :bid_date, :bid_amount)";
    private static final String UPDATE_BID = "UPDATE BIDS SET user_nb = :user_nb, bid_date = :bid_date, bid_amount= :bid_amount WHERE item_nb = :item_nb";
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
    public Bid getBidByItemNumber(int itemNumber) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("item_nb", itemNumber);
        try {
            return namedParameterJdbcTemplate.queryForObject(
                    SELECT_BY_ITEM_ID,
                    namedParameters,
                    new BidDAOImpl.BidRowMapper()
            );
        } catch (EmptyResultDataAccessException ex) {
            // If no bid is found, return a default Bid object with bid amount of zero
            Bid bidEmpty = new Bid();
            bidEmpty.setBidAmount(BigDecimal.ZERO);
            return bidEmpty;
        }
    }


    @Override
    public void insertBid(Bid newBid, Long userNb, int itemNumber) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("user_nb", userNb);
        namedParameters.addValue("item_nb", itemNumber);
        namedParameters.addValue("bid_date", newBid.getBidDate());
        namedParameters.addValue("bid_amount", newBid.getBidAmount());

        namedParameterJdbcTemplate.update(INSERT_INTO, namedParameters);

    }

    @Override
    public void updateBid(Bid newBid, Long userNb) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("user_nb", userNb);
        namedParameters.addValue("item_nb", newBid.getSoldItem().getItemNb());
        namedParameters.addValue("bid_date", newBid.getBidDate());
        namedParameters.addValue("bid_amount", newBid.getBidAmount());
        namedParameterJdbcTemplate.update(UPDATE_BID, namedParameters);

    }

    @Override
    public List<Bid> findAllBidsByUserId(int userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("user_nb", userId);
        List<Bid> bids = namedParameterJdbcTemplate.query(SELECT_ALL_BY_USER_ID, namedParameters, new BidRowMapper());
        return bids;

    }

    public class BidRowMapper implements RowMapper<Bid> {

        @Override
        public Bid mapRow(ResultSet rs, int rowNum) throws SQLException {

            Bid bid = new Bid();
            bid.setBidDate(toLocalDateTime(rs.getTimestamp("b.bid_date")));
            bid.setBidAmount(rs.getBigDecimal("b.bid_amount"));


          //  bid.setBidAmount(rs.getBigDecimal("bidAmount"));

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
            user.setUserNb(rs.getInt("b.user_nb"));
            user.setUsername(rs.getString("u.username"));
            user.setLastname(rs.getString("u.lastname"));
            soldItem.setSoldUser(user);
            bid.setSoldItem(soldItem);
            bid.setUser(user);
            return bid;


        }

        private LocalDateTime toLocalDateTime(java.sql.Timestamp timestamp) {
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        }
    }
}
