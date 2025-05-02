package com.example.dao;

import com.example.db.DatabaseConnection;
import com.example.model.Country;

import java.sql.*;

public class CountryDAO {
    public void create(Country c) throws SQLException {
        String sql = "INSERT INTO countries(name, code, continent_id) VALUES(?,?,?)";
        try (PreparedStatement ps = DatabaseConnection
                .getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getCode());
            ps.setInt(3, c.getContinentId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getInt(1));
                }
            }
        }
    }

    public Country findById(int id) throws SQLException {
        String sql = "SELECT * FROM countries WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection
                .getConnection()
                .prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Country(
                      rs.getInt("id"),
                      rs.getString("name"),
                      rs.getString("code"),
                      rs.getInt("continent_id")
                    );
                }
            }
        }
        return null;
    }

    public Country findByName(String name) throws SQLException {
        String sql = "SELECT * FROM countries WHERE name = ?";
        try (PreparedStatement ps = DatabaseConnection
                .getConnection()
                .prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Country(
                      rs.getInt("id"),
                      rs.getString("name"),
                      rs.getString("code"),
                      rs.getInt("continent_id")
                    );
                }
            }
        }
        return null;
    }
}