/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.db;

import edu.rit.cs.dbc.model.Movie;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

/**
 *
 * @author ptr5201
 */
public class DatabaseConnection {
    
    private static Properties prop = null;
    private static Connection con = null;
    
    private static DatabaseConnection instance = null;
    
    private DatabaseConnection() {
        prop = new Properties();
        
        try {
            prop.load(getClass().getResourceAsStream("db.properties"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public static DatabaseConnection getInstance() {
        if  (instance == null) {
            instance = new DatabaseConnection();
        }
        
        connect();
        
        return instance;
    }
    
    private static void connect() {
        try {
            if (prop != null && 
                    (con == null || (con != null && con.isClosed()))) {
                String connectionUrl = prop.getProperty("db.connectionUrl") 
                        + prop.getProperty("db.username"); 
                Class.forName(prop.getProperty("db.driver"));
                con = DriverManager.getConnection(
                        connectionUrl, 
                        prop.getProperty("db.username"), 
                        prop.getProperty("db.password"));
            }
        } catch (SQLException ex) {
            System.err.println("Could not connect to database");
        } catch (ClassNotFoundException ex) {
            System.err.println("Could not find postgresql Driver");
        }
    }
    
    public boolean memberLogin(String username, String password) {
        if (con != null) {
            try {
                PreparedStatement statement = 
                        con.prepareStatement("select password from member where username = ?");
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    if (digestPassword(password).equals(resultSet.getString("password"))) {
                        return true;
                    }
                }
            } catch (SQLException ex) {
                System.err.println("Error executing member login query");
            }
        }
        
        return false;
    }
    
    public void close() {
        try {
            if (instance != null) {
                if (con != null) {
                    con.close();
                }
            }
        } catch (SQLException ex) {
            System.err.println("Unable to close connection");
        }
    }
    
    public Collection<Movie> getAllMovies() {
        Collection<Movie> allMovies = new ArrayList<>();
        try {
            if (!con.isClosed()) {
                // get all the movies from the database, including each
                // movie's list of genres
                PreparedStatement statement = con.prepareStatement(
                        "select * from movie natural join genre");
                ResultSet resultSet = statement.executeQuery();
                
                // add each movie record to the collection of movies to
                // be returned. a movie with multiple genres will show up
                // in multiple records, but with different genres
                while (resultSet.next()) {
                    Integer movieId = resultSet.getInt("movie_id");
                    String genre = resultSet.getString("genre");
                    
                    // check to see if the current movie matches with
                    // an existing movie that was already added. if this
                    // is the case, then a new genre needs to be added
                    // to the list of genres
                    boolean doesMovieExist = false;
                    if (!allMovies.isEmpty()) {
                        Iterator<Movie> it = allMovies.iterator();
                        while (it.hasNext() && !doesMovieExist) {
                            Movie existingMovie = it.next();
                            if (movieId == existingMovie.getMovieId()) {
                                doesMovieExist = true;
                                existingMovie.getGenres().add(genre);
                            }
                        }
                    }
                    
                    // if a movie hasn't been added to the collection yet,
                    // then create an instance using the values from the
                    // query
                    if (!doesMovieExist) {
                        String title = resultSet.getString("title");
                        String rating = resultSet.getString("rating");
                        String year = resultSet.getString("year");
                        Float score = resultSet.getFloat("score");
                        Collection<String> movieGenres = new ArrayList<>();
                        movieGenres.add(genre);
                        Movie currentMovie = new Movie(
                                title, 
                                rating, 
                                movieGenres, 
                                Integer.parseInt(year), 
                                movieId, 
                                score);
                        
                        allMovies.add(currentMovie);
                    }
                }
                
                statement.close();
            }
        } catch (SQLException sqle) {
            System.err.println("Database access error when retrieving all movies");
        }
        
        return allMovies;
    }
    
    private String digestPassword(String password) {
        password = password + prop.getProperty("db.salt");
        byte input[] = password.getBytes();
        
        try {
            MessageDigest md = MessageDigest.getInstance(
                    prop.getProperty("db.mdAlgorithm"));
            byte output[] = md.digest(input);
            StringBuilder sb = new StringBuilder();
            for (byte b : output) {
                sb.append(String.format("%02x", b & 0xff));
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Message digest algorithm " 
                    + prop.getProperty("db.mdAlgorithm")
                    + " does not exist");
        }
        
        return "";
    }
}
