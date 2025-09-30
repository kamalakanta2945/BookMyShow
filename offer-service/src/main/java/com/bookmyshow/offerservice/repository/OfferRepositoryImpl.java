package com.bookmyshow.offerservice.repository;

import com.bookmyshow.offerservice.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OfferRepositoryImpl implements OfferRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String BASE_SELECT_SQL = "SELECT id, offer_code, description, discount_percentage, max_discount, expiry_date, is_active FROM offers";

    @Override
    public List<Offer> findAll(Pageable pageable, String searchTerm) {
        StringBuilder sql = new StringBuilder(BASE_SELECT_SQL);
        List<Object> params = new ArrayList<>();

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append(" WHERE LOWER(offer_code) LIKE ? OR LOWER(description) LIKE ?");
            String likePattern = "%" + searchTerm.toLowerCase() + "%";
            params.add(likePattern);
            params.add(likePattern);
        }

        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            sql.append(" ORDER BY ");
            List<String> orderClauses = new ArrayList<>();
            for (Sort.Order order : sort) {
                // Basic protection against SQL injection in column names
                if (order.getProperty().matches("[a-zA-Z0-9_]+")) {
                    orderClauses.add(order.getProperty() + " " + order.getDirection());
                }
            }
            sql.append(String.join(", ", orderClauses));
        } else {
            sql.append(" ORDER BY id ASC"); // Default sort
        }

        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageable.getPageSize());
        params.add(pageable.getOffset());

        return jdbcTemplate.query(sql.toString(), params.toArray(), new OfferRowMapper());
    }

    @Override
    public int count(String searchTerm) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM offers");
        List<Object> params = new ArrayList<>();

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append(" WHERE LOWER(offer_code) LIKE ? OR LOWER(description) LIKE ?");
            String likePattern = "%" + searchTerm.toLowerCase() + "%";
            params.add(likePattern);
            params.add(likePattern);
        }

        return jdbcTemplate.queryForObject(sql.toString(), Integer.class, params.toArray());
    }

    @Override
    public Optional<Offer> findById(Long id) {
        try {
            String sql = BASE_SELECT_SQL + " WHERE id = ?";
            Offer offer = jdbcTemplate.queryForObject(sql, new Object[]{id}, new OfferRowMapper());
            return Optional.ofNullable(offer);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Offer save(Offer offer) {
        String sql = "INSERT INTO offers (offer_code, description, discount_percentage, max_discount, expiry_date, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, offer.getOfferCode());
            ps.setString(2, offer.getDescription());
            ps.setDouble(3, offer.getDiscountPercentage());
            ps.setDouble(4, offer.getMaxDiscount());
            ps.setDate(5, java.sql.Date.valueOf(offer.getExpiryDate()));
            ps.setBoolean(6, offer.isActive());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            offer.setId(keyHolder.getKey().longValue());
        }
        return offer;
    }

    @Override
    public int update(Offer offer) {
        String sql = "UPDATE offers SET offer_code = ?, description = ?, discount_percentage = ?, max_discount = ?, expiry_date = ?, is_active = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                offer.getOfferCode(),
                offer.getDescription(),
                offer.getDiscountPercentage(),
                offer.getMaxDiscount(),
                offer.getExpiryDate(),
                offer.isActive(),
                offer.getId());
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM offers WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public int[] saveAll(List<Offer> offers) {
        String sql = "INSERT INTO offers (offer_code, description, discount_percentage, max_discount, expiry_date, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Offer offer = offers.get(i);
                ps.setString(1, offer.getOfferCode());
                ps.setString(2, offer.getDescription());
                ps.setDouble(3, offer.getDiscountPercentage());
                ps.setDouble(4, offer.getMaxDiscount());
                ps.setDate(5, java.sql.Date.valueOf(offer.getExpiryDate()));
                ps.setBoolean(6, offer.isActive());
            }

            @Override
            public int getBatchSize() {
                return offers.size();
            }
        });
    }

    private static final class OfferRowMapper implements RowMapper<Offer> {
        @Override
        public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Offer offer = new Offer();
            offer.setId(rs.getLong("id"));
            offer.setOfferCode(rs.getString("offer_code"));
            offer.setDescription(rs.getString("description"));
            offer.setDiscountPercentage(rs.getDouble("discount_percentage"));
            offer.setMaxDiscount(rs.getDouble("max_discount"));
            offer.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
            offer.setActive(rs.getBoolean("is_active"));
            return offer;
        }
    }
}