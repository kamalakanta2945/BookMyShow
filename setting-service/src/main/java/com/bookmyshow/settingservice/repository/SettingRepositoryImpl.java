package com.bookmyshow.settingservice.repository;

import com.bookmyshow.settingservice.model.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class SettingRepositoryImpl implements SettingRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_BASE = "SELECT id, setting_key, setting_value FROM settings";

    @Override
    public Optional<Setting> findByKey(String key) {
        try {
            String sql = SELECT_BASE + " WHERE setting_key = ?";
            Setting setting = jdbcTemplate.queryForObject(sql, new Object[]{key}, new SettingRowMapper());
            return Optional.ofNullable(setting);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Setting> findAll() {
        String sql = SELECT_BASE;
        return jdbcTemplate.query(sql, new SettingRowMapper());
    }

    @Override
    public Setting save(Setting setting) {
        Optional<Setting> existingSetting = findByKey(setting.getSettingKey());

        if (existingSetting.isPresent()) {
            // Update existing setting
            setting.setId(existingSetting.get().getId());
            String sql = "UPDATE settings SET setting_value = ? WHERE setting_key = ?";
            jdbcTemplate.update(sql, setting.getSettingValue(), setting.getSettingKey());
        } else {
            // Insert new setting
            String sql = "INSERT INTO settings (setting_key, setting_value) VALUES (?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, setting.getSettingKey());
                ps.setString(2, setting.getSettingValue());
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() != null) {
                setting.setId(keyHolder.getKey().longValue());
            }
        }
        return setting;
    }

    private static final class SettingRowMapper implements RowMapper<Setting> {
        @Override
        public Setting mapRow(ResultSet rs, int rowNum) throws SQLException {
            Setting setting = new Setting();
            setting.setId(rs.getLong("id"));
            setting.setSettingKey(rs.getString("setting_key"));
            setting.setSettingValue(rs.getString("setting_value"));
            return setting;
        }
    }
}