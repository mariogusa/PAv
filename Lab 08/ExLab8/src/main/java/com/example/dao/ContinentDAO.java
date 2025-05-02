package com.example.dao;

import com.example.db.DatabaseConnection;
import com.example.model.Continent;

import java.sql.*;

public class ContinentDAO {
    public void create(Continent cont) throws SQLException {
        String sql = "INSERT INTO continents(name) VALUES(?)";
        try (PreparedStatement ps = DatabaseConnection
                .getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cont.getName());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    cont.setId(keys.getInt(1));
                }
            }
        }
    }

    public Continent findById(int id) throws SQLException {
        String sql = "SELECT * FROM continents WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection
                .getConnection()
                .prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Continent(rs.getInt("id"), rs.getString("name"));
                }
            }
        }
        return null;
    }

    public Continent findByName(String name) throws SQLException {
        String sql = "SELECT * FROM continents WHERE name = ?";
        try (PreparedStatement ps = DatabaseConnection
                .getConnection()
                .prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Continent(rs.getInt("id"), rs.getString("name"));
                }
            }
        }
        return null;
    }
}