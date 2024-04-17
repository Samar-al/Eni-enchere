package fr.eni.tp.enienchere.dal.impl;

import fr.eni.tp.enienchere.bo.CollectParcel;
import fr.eni.tp.enienchere.dal.CollectParcelDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CollectParcelDAOImpl implements CollectParcelDAO {
    private static final String INSERT_INTO = "INSERT INTO PARCEL_COLLECTIONS (item_nb, street, zip_code, city) VALUES (:item_nb, :street, :zip_code, :city)";
    private static final String DELETE = "DELETE FROM PARCEL_COLLECTIONS WHERE item_nb = :item_nb";
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CollectParcelDAOImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    @Override
    public void create(CollectParcel collectParcel, Long itemNb) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("item_nb", itemNb);
        namedParameters.addValue("street", collectParcel.getStreet());
        namedParameters.addValue("zip_code", collectParcel.getZipCode());
        namedParameters.addValue("city", collectParcel.getCity());
        namedParameterJdbcTemplate.update(INSERT_INTO, namedParameters);
    }

    @Override
    public void delete(long itemId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("item_nb", itemId);
        namedParameterJdbcTemplate.update(DELETE, namedParameters);
    }
}
