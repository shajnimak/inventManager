package com.inventory.DAO;

import com.inventory.Database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class FAQDAO {
    Connection conn;
    Statement statement;
    ResultSet resultSet;

    public FAQDAO() {
        try {
            conn = new ConnectionFactory().getConn();
            statement = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Получить все вопросы и ответы
    public ResultSet getAllFAQs() {
        try {
            String query = "SELECT * FROM faq ORDER BY id ASC";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // Поиск по ключевому слову (вопросу или ответу)
    public ResultSet searchFAQs(String keyword) {
        try {
            String query = "SELECT * FROM faq WHERE LOWER(question) LIKE ? OR LOWER(answer) LIKE ? ORDER BY id ASC";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            ps.setString(2, "%" + keyword.toLowerCase() + "%");
            resultSet = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public int getFAQCount() {
        try {
            String query = "SELECT COUNT(*) AS total FROM faq";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return resultSet.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
