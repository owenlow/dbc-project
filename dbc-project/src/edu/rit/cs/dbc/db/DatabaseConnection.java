/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.db;

import edu.rit.cs.dbc.model.Member;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ptr5201
 */
public class DatabaseConnection {
    
    private static Properties prop = null;
    private static Connection con = null;
    private static Member currentMember = null;
    
    private static DatabaseConnection instance = null;
    
    private DatabaseConnection() {
        prop = new Properties();
        
        try {
            prop.load(getClass().getResourceAsStream("db.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.err.println("Could not find postgresql Driver");
            ex.printStackTrace();
        }
    }
    
    public boolean memberLogin(String username, String password) {
        boolean validLogin = false;
        
        if (con != null) {
            try {
                PreparedStatement statement = 
                        con.prepareStatement("select * from member where username = ?");
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next() && !validLogin) {
                    if (digestPassword(password).equals(resultSet.getString("password"))) {
                        currentMember = new Member(
                                resultSet.getInt("member_id"), 
                                resultSet.getString("fullname"), 
                                resultSet.getString("username")
                        );
                        validLogin = true;
                    }
                }
            } catch (SQLException ex) {
                System.err.println("Error executing member login query");
                ex.printStackTrace();
            }
        }
        
        return validLogin;
    }
    
    public boolean memberExists( String username ) {
        boolean memberExists = false;
        
        if (con != null) {
            try {
                PreparedStatement statement = 
                        con.prepareStatement("select * from member where username = ?");
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next() && !memberExists) {
                    if (username.equals(resultSet.getString("username"))) {
                        memberExists = true;
                    }
                }
            } catch (SQLException ex) {
                System.err.println("Error checking for member existance");
                ex.printStackTrace();
            }
        }
        
        return memberExists;
    }
    
    public boolean createMember( String username, String fullname, String password ) {
        boolean successfulMemberCreation = false;
        
        if (con != null) {
            try {
                PreparedStatement statement = 
                        con.prepareStatement("insert into member (username, fullname, password) values ( ?, ?, ? )");
                statement.setString(1, username);
                statement.setString(2, fullname);
                statement.setString(3, digestPassword(password));
                
                statement.execute();
                successfulMemberCreation = true;
            } catch (SQLException ex) {
                System.err.println("Error inserting new member");
                ex.printStackTrace();
            }
        }
        
        return successfulMemberCreation;
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
            ex.printStackTrace();
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
                        Movie movieResult = new Movie(
                                title, 
                                rating, 
                                movieGenres, 
                                Integer.parseInt(year), 
                                movieId, 
                                score);
                        allMovies.add(movieResult);
                    }
                }
                
                statement.close();
            }
        } catch (SQLException sqle) {
            System.err.println("Database error when retrieving all movies");
            sqle.printStackTrace();
        }
        
        return allMovies;
    }

    public Collection<Movie> getQueueMovies() {
        Collection<Movie> queueMovies = new ArrayList<>();
        
        if (currentMember != null) {
            try {
                if (!con.isClosed()) {
                    // get all the movies from the database, including each
                    // movie's list of genres
                    PreparedStatement statement = con.prepareStatement(
                            "select * from movie"
                            + " natural join genre"
                            + " natural join queue where"
                            + " member_id = ?"
                            + " order by rank asc");
                    statement.setInt(1, currentMember.getMemberId());
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
                        if (!queueMovies.isEmpty()) {
                            Iterator<Movie> it = queueMovies.iterator();
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
                            Movie movieResult = new Movie(
                                    title, 
                                    rating, 
                                    movieGenres, 
                                    Integer.parseInt(year), 
                                    movieId, 
                                    score);
                            queueMovies.add(movieResult);
                        }
                    }

                    statement.close();
                }
            } catch (SQLException sqle) {
                System.err.println("Database error when"
                        + " retrieving movies in member's queue");
                sqle.printStackTrace();
            }
        }
        
        return queueMovies;
    }
    
    public Collection<Movie> getRecentMovies() {
        Collection<Movie> recentMovies = new ArrayList<Movie>();
        
        if (currentMember != null) {
            try {
                if (!con.isClosed()) {
                    // get all the movies from the database, including each
                    // movie's list of genres
                    PreparedStatement statement = con.prepareStatement(
                            "select * from movie"
                            + " natural join genre"
                            + " natural join recent where"
                            + " member_id = ?"
                            + " order by timestamp desc");
                    statement.setInt(1, currentMember.getMemberId());
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
                        if (!recentMovies.isEmpty()) {
                            Iterator<Movie> it = recentMovies.iterator();
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
                            Movie movieResult = new Movie(
                                    title, 
                                    rating, 
                                    movieGenres, 
                                    Integer.parseInt(year), 
                                    movieId, 
                                    score);
                            recentMovies.add(movieResult);
                        }
                    }

                    statement.close();
                }
            } catch (SQLException sqle) {
                System.err.println("Database error when"
                        + " retrieving movies in member's recent");
                sqle.printStackTrace();
            }
        }
        
        return recentMovies;
    }
    
    public Collection<Movie> getPurchasedMovies() {
        
        Collection<Movie> purchasedMovies = new ArrayList<Movie>();
        
        if (currentMember != null) {
            try {
                if (!con.isClosed()) {
                    // get all the movies from the database, including each
                    // movie's list of genres
                    PreparedStatement statement = con.prepareStatement(
                            "select * from movie"
                            + " natural join genre"
                            + " natural join purchase where"
                            + " member_id = ?"
                            + " order by timestamp desc");
                    statement.setInt(1, currentMember.getMemberId());
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
                        if (!purchasedMovies.isEmpty()) {
                            Iterator<Movie> it = purchasedMovies.iterator();
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
                            Movie movieResult = new Movie(
                                    title, 
                                    rating, 
                                    movieGenres, 
                                    Integer.parseInt(year), 
                                    movieId, 
                                    score);
                            purchasedMovies.add(movieResult);
                        }
                    }

                    statement.close();
                }
            } catch (SQLException sqle) {
                System.err.println("Database error when"
                        + " retrieving movies in member's recent");
                sqle.printStackTrace();
            }
        }
        
        return purchasedMovies;
        
    }
    
    public void addMoviesToQueue(Collection<Movie> moviesSelected) {
        if (currentMember != null) {
            try {
                if (!con.isClosed()) {
                    for (Movie movieToQueue : moviesSelected) {
                        PreparedStatement statement = con.prepareStatement(
                                "select rank from queue"
                                + " where rank = ("
                                    + "select max(rank) from queue "
                                    + "where member_id = ?)"
                                + " and member_id = ?");
                        statement.setInt(1, currentMember.getMemberId());
                        statement.setInt(2, currentMember.getMemberId());
                        ResultSet result = statement.executeQuery();
                        int newRank = 0;
                        while (result.next()) {
                            newRank = result.getInt("rank");
                        }
                        
                        statement = con.prepareStatement(
                                "insert into queue"
                                + " (member_id, movie_id, rank)"
                                + " values (?, ?, ?)");
                        statement.setInt(1, currentMember.getMemberId());
                        statement.setInt(2, movieToQueue.getMovieId());
                        statement.setInt(3, newRank + 1);
                        int insertResult = statement.executeUpdate();
                        statement.close();
                    }
                }
            } catch (SQLException ex) {
                System.err.println("Database error when adding"
                        + " movies to a member's queue");
                ex.printStackTrace();
            }
        }
    }

    public void swapMovieRank(Movie currentMovie, Movie otherMovie) {
        if (currentMember != null) {
            try {
                if (!con.isClosed()) {
                    PreparedStatement statement = con.prepareStatement(
                            "update queue set rank = queue_temp.rank"
                            + " from queue as queue_temp where"
                            + " (queue.movie_id = ? or queue_temp.movie_id = ?)"
                            + " and (queue.movie_id = ? or queue_temp.movie_id = ?)"
                            + " and ((queue.movie_id <> queue_temp.movie_id)"
                            + " and (queue.member_id = ? and queue_temp.member_id = ?))");
                    statement.setInt(1, currentMovie.getMovieId());
                    statement.setInt(2, currentMovie.getMovieId());
                    statement.setInt(3, otherMovie.getMovieId());
                    statement.setInt(4, otherMovie.getMovieId());
                    statement.setInt(5, currentMember.getMemberId());
                    statement.setInt(6, currentMember.getMemberId());
                    int insertResult = statement.executeUpdate();
                    statement.close();
                }
            } catch (SQLException ex) {
                System.err.println("Database error when swapping ranks"
                        + "of two movies within a member's queue");
                ex.printStackTrace();
            }
        }
    }
    
    private String digestPassword(String password) {
        String digestedPassword = "";
        
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
            
            digestedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Message digest algorithm " 
                    + prop.getProperty("db.mdAlgorithm")
                    + " does not exist");
        }
        
        return digestedPassword;
    }
    
    /**
     * NOTE: this method throws a NullPointerException if called
     * from one of methods that returns a collection of movies;
     * needs fixing
     * @param resultSet
     * @param movieCollection
     * @return 
     */
    private Movie constructMovie(ResultSet resultSet, Collection<Movie> movieCollection) {
        Movie movieResult = null;
        
        try {
            System.out.println(Thread.currentThread().toString() + ": getting movieId and genre");
            Integer movieId = resultSet.getInt("movie_id");
            String genre = resultSet.getString("genre");

            // check to see if the current movie matches with
            // an existing movie that was already added. if this
            // is the case, then a new genre needs to be added
            // to the list of genres
            boolean doesMovieExist = false;
            if (!movieCollection.isEmpty()) {
                System.out.println(Thread.currentThread().toString() + ": movieCollection not empty");
                Iterator<Movie> it = movieCollection.iterator();
                System.out.println(Thread.currentThread().toString() + ": got iterator");
                while (it.hasNext() && !doesMovieExist) {
                    System.out.println(Thread.currentThread().toString() + ": iterator has next and movie not found yet");
                    Movie existingMovie = it.next();
                    System.out.println(Thread.currentThread().toString() + ": got a movie: " + existingMovie);
                    System.out.println(Thread.currentThread().toString() + ": movieId(" + movieId + ") == existingMovie.ID(" + existingMovie.getMovieId() + ")");
                    if (movieId.intValue() == existingMovie.getMovieId()) {
                        System.out.println(Thread.currentThread().toString() + ": movie matched");
                        doesMovieExist = true;
                        existingMovie.getGenres().add(genre);
                        System.out.println(Thread.currentThread().toString() + ": genre added: " + genre);
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
                movieResult = new Movie(
                        title, 
                        rating, 
                        movieGenres, 
                        Integer.parseInt(year), 
                        movieId, 
                        score);
            }
        } catch (SQLException sqle) {
            System.err.println("Constructing movie with invalid result set");
            sqle.printStackTrace();
        }
        
        return movieResult;
    }
}
