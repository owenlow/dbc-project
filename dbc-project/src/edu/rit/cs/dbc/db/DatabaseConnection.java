/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.db;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ptr5201
 */
public class DatabaseConnection {
    
    private Properties prop = null;
    private Connection con = null;
    
    public DatabaseConnection() {
        prop = new Properties();
        
        try {
            prop.load(getClass().getResourceAsStream("db.properties"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public void connect(String username, String password) {
        if (prop != null) {
            if (username.equals(prop.getProperty("db.username")) && 
                    digestPassword(password).equals(prop.getProperty("db.password"))) {
                
                try {
                    String connectionUrl = "jdbc:postgresql://reddwarf.cs.rit.edu/" + username; 
                    Class.forName("org.postgresql.Driver");
                    con = DriverManager.getConnection(connectionUrl, username, password);
                } catch (SQLException ex) {
                    System.err.println("Could not connect to database");
                } catch (ClassNotFoundException ex) {
                    System.err.println("Could not find postgresql Driver");
                }
                
            } else {
                System.out.println("Non-matching credentials");
            }
        }
    }
    
    public void close() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            System.err.println("Unable to close connection");
        }
    }
    
    private String digestPassword(String password) {
        password = password + prop.getProperty("db.salt");
        byte input[] = password.getBytes();
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte output[] = md.digest(input);
            StringBuilder sb = new StringBuilder();
            for (byte b : output) {
                sb.append(String.format("%02x", b & 0xff));
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        
        return "";
    }
}
