package fr.eni.tp.enienchere.dal.impl;

import fr.eni.tp.enienchere.bll.SoldItemService;
import fr.eni.tp.enienchere.bo.*;
import fr.eni.tp.enienchere.dal.SoldItemDAO;
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
public class SoldItemDAOImpl implements SoldItemDAO {

    private static final String SELECT_BY_ID= "SELECT s.item_nb, s.item_name, s.description, s.start_bid_date, s.end_bid_date, s.initial_price, s.sale_price,s.category_nb, s.user_nb, s.sales_status, u.user_nb, u.username, u.phone, pc.street, pc.zip_code, pc.city, c.wording FROM SOLD_ITEMS s LEFT JOIN USERS as u ON s.user_nb = u.user_nb LEFT JOIN PARCEL_COLLECTIONS as pc ON s.item_nb = pc.item_nb LEFT JOIN CATEGORY as c ON s.category_nb = c.category_nb WHERE s.item_nb=:id";
    private static final String INSERT_INTO = "INSERT INTO SOLD_ITEMS (item_name, description, start_bid_date, end_bid_date, initial_price, sale_price, user_nb, category_nb, sales_status) VALUES (:item_name, :description, :start_bid_date, :end_bid_date, :initial_price, :sale_price, :user_nb, :category_nb, :sales_status)";
    private static final String SELECT_ALL = "SELECT s.user_nb, s.item_nb, s.item_name, s.description, s.start_bid_date, s.category_nb, s.end_bid_date, s.initial_price, s.sales_status, s.sale_price, u.user_nb, u.username, u.phone, pc.street, pc.zip_code, pc.city, c.wording FROM SOLD_ITEMS s LEFT JOIN USERS as u ON s.user_nb = u.user_nb LEFT JOIN PARCEL_COLLECTIONS as pc ON s.item_nb = pc.item_nb LEFT JOIN CATEGORY as c ON s.category_nb = c.category_nb WHERE s.sales_status != 2";

    private static final String SELECT_ALL_FOR_FILTER = "SELECT s.user_nb, s.item_nb, s.item_name, s.description, s.start_bid_date, s.category_nb, s.end_bid_date, s.initial_price, s.sales_status, s.sale_price, u.user_nb, u.username, u.phone, pc.street, pc.zip_code, pc.city, c.wording FROM SOLD_ITEMS s LEFT JOIN USERS as u ON s.user_nb = u.user_nb LEFT JOIN PARCEL_COLLECTIONS as pc ON s.item_nb = pc.item_nb LEFT JOIN CATEGORY as c ON s.category_nb = c.category_nb LEFT JOIN bids as b ON s.item_nb = b.item_nb WHERE s.item_name LIKE :filter";

    private static final String UPDATE = "UPDATE SOLD_ITEMS SET item_name= :item_name, description= :description, start_bid_date= :start_bid_date, end_bid_date= :end_bid_date, initial_price= :initial_price, sale_price= :sale_price, user_nb = :user_nb, category_nb= :category_nb ,sales_status= :sales_status WHERE item_nb = :item_nb";
    private static final String SELECT_ALL_BY_USER_ID= "SELECT s.item_nb, s.item_name, s.description, s.start_bid_date, s.end_bid_date, s.initial_price, s.sale_price,s.category_nb, s.user_nb, s.sales_status, u.user_nb, u.username, u.phone, pc.street, pc.zip_code, pc.city, c.wording FROM SOLD_ITEMS s LEFT JOIN USERS as u ON s.user_nb = u.user_nb LEFT JOIN PARCEL_COLLECTIONS as pc ON s.item_nb = pc.item_nb LEFT JOIN CATEGORY as c ON s.category_nb = c.category_nb WHERE s.user_nb = :userId";

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
             //  new BeanPropertyRowMapper<>(SoldItem.class)
              new SoldItemDAOImpl.SoldItemRowMapper()
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

    @Override
    public void update(SoldItem soldItem) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("item_name", soldItem.getItemName());
        namedParameters.addValue("description", soldItem.getDescription());
        namedParameters.addValue("start_bid_date", soldItem.getDateStartBid());
        namedParameters.addValue("end_bid_date", soldItem.getDateEndBid());
        namedParameters.addValue("initial_price", soldItem.getInitialPrice());
        namedParameters.addValue("sale_price", soldItem.getSalePrice());
        namedParameters.addValue("sales_status", soldItem.getSaleStatus());
        namedParameters.addValue("category_nb", soldItem.getCategory().getCategoryNb());
        namedParameters.addValue("user_nb", soldItem.getSoldUser().getUserNb());
        namedParameters.addValue("item_nb", soldItem.getItemNb());
        namedParameterJdbcTemplate.update(UPDATE, namedParameters);

    }

    @Override
    public List<SoldItem> findAll() {
        List<SoldItem>soldItems = jdbcTemplate.query(SELECT_ALL, new SoldItemDAOImpl.SoldItemRowMapper());
        return soldItems;
    }

    @Override
    public List<SoldItem> search(String filter, Integer category, long userNb, Integer openBids, Integer myCurrentBids, Integer wonBids, Integer currentSale, Integer salesNotStarted, Integer completedSales) {
        StringBuilder sql = new StringBuilder(SELECT_ALL_FOR_FILTER);
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("filter", "%" + filter + "%");

        if (category != null) {
            sql.append(" AND s.category_nb = :category");
            namedParameters.addValue("category", category);
        }

        // Si un des trois éléments sont sélectionnés
        if (openBids != null || myCurrentBids != null || wonBids != null) {

            // On sélectionne "Enchères ouvertes", "Mes enchères en cours", "Mes enchères remportées"
            if (openBids != null && myCurrentBids != null && wonBids != null ) {
                sql.append(" AND b.user_nb = :userId");
                namedParameters.addValue("userId", userNb);
                sql.append(" AND sales_status IN (:openBids, :myCurrentBids, :wonBids)");
                namedParameters.addValue("openBids", openBids);
                namedParameters.addValue("myCurrentBids", myCurrentBids);
                namedParameters.addValue("wonBids", wonBids);

            // On sélectionne "Enchères ouvertes", "Mes enchères en cours"
            } else if (openBids != null && myCurrentBids != null) {
                sql.append(" AND b.user_nb = :userId");
                namedParameters.addValue("userId", userNb);
                sql.append(" AND sales_status IN (:openBids, :myCurrentBids)");
                namedParameters.addValue("openBids", openBids);
                namedParameters.addValue("myCurrentBids", myCurrentBids);

            // On sélectionne "Enchères ouvertes", "Mes enchères remportées"
            } else if (openBids != null && wonBids != null) {
                sql.append(" AND b.user_nb = :userId");
                namedParameters.addValue("userId", userNb);
                sql.append(" AND sales_status IN (:openBids, :wonBids)");
                namedParameters.addValue("openBids", openBids);
                namedParameters.addValue("wonBids", wonBids);

            // On sélectionne "Mes enchères en cours", "Mes enchères remportées"
            } else if (myCurrentBids != null && wonBids != null) {
                sql.append(" AND b.user_nb = :userId");
                namedParameters.addValue("userId", userNb);
                sql.append(" AND sales_status IN (:myCurrentBids, :wonBids)");
                namedParameters.addValue("myCurrentBids", myCurrentBids);
                namedParameters.addValue("wonBids", wonBids);
            } else {
                // On sélectionne "Enchères ouvertes"
                if (openBids != null) {
                    sql.append(" AND sales_status = :openBids");
                    namedParameters.addValue("openBids", openBids);
                }

                // On sélectionne "Mes enchères remportées
                if (myCurrentBids != null) {
                    sql.append(" AND b.user_nb = :userId");
                    namedParameters.addValue("userId", userNb);
                    sql.append(" AND sales_status = :myCurrentBids");
                    namedParameters.addValue("myCurrentBids", myCurrentBids);
                }

                if (wonBids != null) {
                    sql.append(" AND b.user_nb = :userId");
                    namedParameters.addValue("userId", userNb);
                    sql.append(" AND sales_status = :wonBids");
                    namedParameters.addValue("wonBids", wonBids);
                }
            }
        }

        // Si un élément sur les trois est sélectionné
        if (currentSale != null || salesNotStarted != null || completedSales != null) {
            sql.append(" AND u.user_nb = :userId");
            namedParameters.addValue("userId", userNb);

            // On sélectionne "Mes ventes en cours", "Ventes non débutées", "Ventes terminées"
            if (currentSale != null && salesNotStarted != null && completedSales != null) {
                sql.append(" AND sales_status IN (:currentSale, :salesNotStarted, :completedSales);");
                namedParameters.addValue("currentSale", currentSale);
                namedParameters.addValue("salesNotStarted", salesNotStarted);
                namedParameters.addValue("completedSales", completedSales);

            // On sélectionne "Mes ventes en cours" et "Ventes non débutées"
            } else if (currentSale!= null && salesNotStarted != null) {
                sql.append(" AND sales_status IN (:currentSale, :salesNotStarted);");
                namedParameters.addValue("currentSale", currentSale);
                namedParameters.addValue("salesNotStarted", salesNotStarted);

            // On sélectionne "Mes ventes en cours" et "Ventes terminées"
            } else if (currentSale!= null && completedSales != null) {
                sql.append(" AND sales_status IN (:currentSale, :completedSales);");
                namedParameters.addValue("currentSale", currentSale);
                namedParameters.addValue("completedSales", completedSales);

            // On sélectionne "Ventes non débutées" et "Ventes terminées"
            } else if (salesNotStarted != null && completedSales != null) {
                sql.append(" AND sales_status IN (:salesNotStarted, :completedSales);");
                namedParameters.addValue("salesNotStarted", salesNotStarted);
                namedParameters.addValue("completedSales", completedSales);

            } else {
                //On sélectionne que "Mes ventes en cours"
                if (currentSale != null) {
                    sql.append(" AND sales_status = :currentSale");
                    namedParameters.addValue("currentSale", currentSale);
                }

                //On sélectionne que "Ventes non débutées"
                if (salesNotStarted != null) {
                    sql.append(" AND sales_status = :salesNotStarted");
                    namedParameters.addValue("salesNotStarted", salesNotStarted);
                }

                //On sélectionne que "Ventes terminées"
                if (completedSales != null) {
                    sql.append(" AND sales_status = :completedSales");
                    namedParameters.addValue("completedSales", completedSales);
                }
            }
        }


        List<SoldItem>soldItems = namedParameterJdbcTemplate.query(sql.toString(), namedParameters,new SoldItemDAOImpl.SoldItemRowMapper());
        return soldItems;
    }

    @Override
    public List<SoldItem> findAllByUserId(int userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        List<SoldItem>soldItems = namedParameterJdbcTemplate.query(SELECT_ALL_BY_USER_ID, namedParameters, new SoldItemDAOImpl.SoldItemRowMapper());
        return soldItems;
    }

    public class SoldItemRowMapper implements RowMapper<SoldItem> {

        @Override
        public SoldItem mapRow(ResultSet rs, int rowNum) throws SQLException {

            SoldItem soldItem = new SoldItem();
            soldItem.setItemNb(rs.getInt("s.item_nb"));
            soldItem.setItemName(rs.getString("s.item_name"));
            soldItem.setDescription(rs.getString("s.description"));
            soldItem.setDateStartBid(rs.getDate("s.start_bid_date"));
            soldItem.setDateEndBid(rs.getDate("s.end_bid_date"));
            soldItem.setInitialPrice(rs.getInt("s.initial_price"));
            soldItem.setSalePrice(rs.getInt("s.sale_price"));
            soldItem.setSaleStatus(rs.getInt("s.sales_status"));

            CollectParcel collectParcel = new CollectParcel();
            collectParcel.setStreet(rs.getString("pc.street"));
            collectParcel.setZipCode(rs.getString("pc.zip_code"));
            collectParcel.setCity(rs.getString("pc.city"));
            soldItem.setCollectParcel(collectParcel);

            Category category = new Category();
            category.setCategoryNb(rs.getInt("s.category_nb"));
            category.setWording(rs.getString("c.wording"));
            soldItem.setCategory(category);

            User user = new User();
            user.setUserNb(rs.getInt("s.user_nb"));
            user.setUsername(rs.getString("u.username"));
            user.setPhone(rs.getString("u.phone"));

            user.setStreet(rs.getString("pc.street"));
            user.setZipCode(rs.getString("pc.zip_code"));
            user.setCity(rs.getString("pc.city"));

            soldItem.setSoldUser(user);


            return soldItem;


        }

        private LocalDateTime toLocalDateTime(java.sql.Timestamp timestamp) {
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        }

    }
}
