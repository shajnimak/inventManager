/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inventory.Database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 *
 * @author asjad
 */

//Class to retrieve connection for database and login verfication.
public class ConnectionFactory {

    static final String driver = "org.postgresql.Driver";
    static final String url = "jdbc:postgresql://localhost:5432/inventory";
    static String username;
    static String password;

    Properties prop;

    Connection conn = null;
    Statement statement = null;
    ResultSet resultSet = null;

    public ConnectionFactory() {
        try {
            // Load credentials from XML
            prop = new Properties();
            prop.loadFromXML(new FileInputStream("lib/DBCredentials.xml"));

            username = prop.getProperty("username");
            password = prop.getProperty("password");

            // Load the PostgreSQL driver
            Class.forName(driver);

            // Establish connection
            conn = DriverManager.getConnection(url, username, password);
            statement = conn.createStatement();  // Initialize statement here

        } catch (IOException e) {
            System.err.println("Error loading database credentials.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Database connection failed.");
            e.printStackTrace();
        }
    }


    public Connection getConn() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    //Login verification method
    public boolean checkLogin(String username, String password, String userType) {
        String query = "SELECT 1 FROM users WHERE username=? AND password=? AND usertype=? FETCH FIRST 1 ROW ONLY";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, userType);
            resultSet = pstmt.executeQuery();
            return resultSet.next();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
