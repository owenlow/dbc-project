/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.db;

import edu.rit.cs.dbc.model.Member;
import edu.rit.cs.dbc.model.Movie;
import edu.rit.cs.dbc.model.Purchase;
import edu.rit.cs.dbc.model.Recent;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

/**
 * A database connection object that provides operations
 * to access and modify data in the database
 */
public class DatabaseConnection {
    
    // database information for connecting to the database
    private static Properties prop = null;
    
    // the connection object to create and execute SQL statements from
    private static Connection con = null;
    
    // the member that has currently logged into the application
    private static Member currentMember = null;
    
    // singleton to ensure that there is only one instance
    // of this application ever created
    private static DatabaseConnection instance = null;
    
    /**
     * A private constructor for setting up the connection
     * information and creating the instance
     */
    private DatabaseConnection() {
        prop = new Properties();
        
        try {
            prop.load(getClass().getResourceAsStream("db.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Returns the instance of this class, and creates the
     * instance if necessary
     * @return an instance of this class that is connected
     *         to the database
     */
    public static DatabaseConnection getInstance() {
        if  (instance == null) {
            instance = new DatabaseConnection();
        }
        
        // make sure we are connected to the database
        // even if the instance is already created
        connect();
        
        return instance;
    }
    
    /**
     * Create and establish the connection to the database using
     * the database information from the properties file
     */
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
    
    /**
     * Attempt to authenticate a member and set the current
     * member of this session if the login was successful
     * @param username the member's username
     * @param password the member's password
     * @return true if authentication was successful; false if otherwise
     */
    public boolean memberLogin(String username, String password) {
        boolean validLogin = false;
        
        if (con != null) {
            try {
                // get the necessary data for the specified username
                PreparedStatement statement = 
                        con.prepareStatement("select * from member where username = ?");
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next() && !validLogin) {
                    // digest the password specified by the user and 
                    // check whether or not that digest matches the 
                    // digested password in the database
                    if (digestPassword(password).equals(resultSet.getString("password"))) {
                        // the authentication was successful, so set 
                        // the session's current member to the user who
                        // successfully authenticated
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
    
    /**
     * Determine whether or not a member exists in the database
     * @param username a member's username
     * @return true if the member exists in the database; false if otherwise
     */
    public boolean memberExists( String username ) {
        boolean memberExists = false;
        
        if (con != null) {
            try {
                // perform a SQL query to find data with the matching username
                PreparedStatement statement = con.prepareStatement(
                        "select * from member where username = ?"
                        );
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next() && !memberExists) {
                    // if the specified username matches the result from the
                    // SQL query, then the member does exist
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
    
    /**
     * Register a new member's login credentials into the database
     * @param username the new member's username
     * @param fullname the new member's full name
     * @param password the new member's password
     * @return true if the member was successfully created; false if otherwise
     */
    public boolean createMember( String username, String fullname, String password ) {
        boolean successfulMemberCreation = false;
        
        if (con != null) {
            try {
                // perform a SQL statement to insert the member's login
                // credentials into the database
                PreparedStatement statement = con.prepareStatement(
                        "insert into member"
                        + " (username, fullname, password)"
                        + " values ( ?, ?, ? )"
                        );
                statement.setString(1, username);
                statement.setString(2, fullname);
                // make sure the member's password in not in plain text
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
    
    /**
     * Close the connection to the database
     */
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
    
    /**
     * Get the entire collection of movies from the database
     * @return all the movies from the database
     */
    public Collection<Movie> getAllMovies() {
        Collection<Movie> allMovies = new ArrayList<Movie>();
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
                        Collection<String> movieGenres = new ArrayList<String>();
                        movieGenres.add(genre);
                        Movie movieResult = new Movie( 
                                movieId, 
                                title, 
                                rating, 
                                movieGenres, 
                                Integer.parseInt(year),
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

    /**
     * Get the collection of the current member's movies that
     * they have added to their queue
     * @return the current member's queue of movies
     */
    public Collection<Movie> getQueueMovies() {
        Collection<Movie> queueMovies = new ArrayList<Movie>();
        
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
                            Collection<String> movieGenres = new ArrayList<String>();
                            movieGenres.add(genre);
                            Movie movieResult = new Movie(
                                    movieId, 
                                    title, 
                                    rating, 
                                    movieGenres, 
                                    Integer.parseInt(year), 
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
    
    /**
     * Get the current member's collection of movies that have
     * been recently watched
     * @return the current member's collection of recently watched movies
     */
    public Collection<Recent> getRecentMovies() {
        // the collection of the member's movies in their queue
        Collection<Movie> memberMovies = new ArrayList<Movie>();
        
        // the collection of the member's movies that have been
        // recently watched
        Collection<Recent> recentMovies = new ArrayList<Recent>();
        
        if (currentMember != null) {
            try {
                if (!con.isClosed()) {
                    // get all the movies from the database, including each
                    // movie's list of genres, ordered by the latest timestamp
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
                            Iterator<Movie> it = memberMovies.iterator();
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
                            Collection<String> movieGenres = new ArrayList<String>();
                            movieGenres.add(genre);
                            Movie movieResult = new Movie(
                                    movieId, 
                                    title, 
                                    rating, 
                                    movieGenres, 
                                    Integer.parseInt(year), 
                                    score);
                            memberMovies.add(movieResult);
                        }
                    }
                    
                    // filter out any movies from the member's collection of
                    // movies that have not been recently watched
                    if (!memberMovies.isEmpty()) {
                        statement = con.prepareStatement(
                                "select * from recent"
                                + " where member_id = ?"
                                + " order by timestamp desc"
                                );
                        statement.setInt(1, currentMember.getMemberId());
                        resultSet = statement.executeQuery();
                        while (resultSet.next()) {
                            // get the information about a recently 
                            // watched movie
                            Integer movieId = resultSet.getInt("movie_id");
                            Integer watchcount = resultSet.getInt("watchcount");
                            Timestamp timestamp = resultSet.getTimestamp("timestamp");
                            Movie movieResult = null;
                            
                            // get the recently watched movie instance whose 
                            // id matches with a movie from the member's
                            // collection of movies
                            for (Movie m : memberMovies) {
                                if (movieId.equals(m.getMovieId())) {
                                    movieResult = m;
                                    break;
                                }
                            }
                            
                            // construct a recently watched movie instance
                            // and add it to the collection to return
                            Recent recentResult = new Recent(
                                    watchcount.intValue(),
                                    timestamp,
                                    movieResult,
                                    currentMember
                                    );
                            recentMovies.add(recentResult);
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
    
    /**
     * Get the current member's collection of movies that have
     * been purchased
     * @return the current member's collection of purchased movies
     */
    public Collection<Purchase> getPurchasedMovies() {
        // the collection of the member's movies in their queue
        Collection<Movie> memberMovies = new ArrayList<Movie>();
        
        // the collection of the member's movies that have been
        // purchased
        Collection<Purchase> purchasedMovies = new ArrayList<Purchase>();
        
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
                            Iterator<Movie> it = memberMovies.iterator();
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
                            Collection<String> movieGenres = new ArrayList<String>();
                            movieGenres.add(genre);
                            Movie movieResult = new Movie(
                                    movieId, 
                                    title, 
                                    rating, 
                                    movieGenres, 
                                    Integer.parseInt(year), 
                                    score
                                    );
                            memberMovies.add(movieResult);
                        }
                    }
                    
                    // filter out any movies from the member's collection of
                    // movies that have not been purchased
                    if (!memberMovies.isEmpty()) {
                        statement = con.prepareStatement(
                                "select * from purchase"
                                + " where member_id = ?"
                                + " order by timestamp desc"
                                );
                        statement.setInt(1, currentMember.getMemberId());
                        resultSet = statement.executeQuery();
                        while (resultSet.next()) {
                            // get the information about a purchased movie
                            Integer movieId = resultSet.getInt("movie_id");
                            Double price = resultSet.getDouble("price");
                            Timestamp timestamp = resultSet.getTimestamp("timestamp");
                            Movie movieResult = null;
                            // get the purchased movie instance whose 
                            // id matches with a movie from the member's
                            // collection of movies
                            for (Movie m : memberMovies) {
                                if (movieId.equals(m.getMovieId())) {
                                    movieResult = m;
                                    break;
                                }
                            }
                            
                            // construct a purchased movie instance
                            // and add it to the collection to return
                            Purchase purchaseResult = new Purchase(
                                    price.doubleValue(),
                                    timestamp,
                                    movieResult,
                                    currentMember
                                    );
                            purchasedMovies.add(purchaseResult);
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
    
    /**
     * Add movies to the current member's queue
     * @param moviesSelected movies selected from the table in the 
     *                       "Browse Movies" screen
     */
    public void addMoviesToQueue(Collection<Movie> moviesSelected) {
        if (currentMember != null) {
            try {
                if (!con.isClosed()) {
                    // get the maximum rank for existing movies in the current
                    // member's queue and increase that rank by 1, which is to
                    // be inserted for each selected movie, into the current
                    // member's queue
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
                        
                        // after getting the maximum rank, if it exists,
                        // increment the rank's value
                        int newRank = 0;
                        while (result.next()) {
                            newRank = result.getInt("rank");
                        }
                        newRank++;
                        
                        // prepare the SQL statement to insert a 
                        // movie into the current member's queue
                        statement = con.prepareStatement(
                                "insert into queue"
                                + " (member_id, movie_id, rank)"
                                + " values (?, ?, ?)"
                                );
                        statement.setInt(1, currentMember.getMemberId());
                        statement.setInt(2, movieToQueue.getMovieId());
                        statement.setInt(3, newRank);
                        int insertResult = statement.executeUpdate();
                        
                        statement.close();
                    }
                }
            } catch (PSQLException ex) {
                // show an error message if a movie that the member wished
                // to add to their queue already exists in their queue
                ServerErrorMessage errorMessage = ex.getServerErrorMessage();
                if (errorMessage.getMessage().equals(
                        "duplicate key value violates unique constraint \"queue_pkey\"")) {
                    JOptionPane.showMessageDialog(
                            null, 
                            "Cannot have duplicate movies in a queue", 
                            "Duplicate Movie", 
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                System.err.println("Database error when adding"
                        + " movies to a member's queue");
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Purchase a movie by adding it to their collection of purchased movies
     * @param movie the movie to purchase
     */
    public void addMovieToPurchased(Movie movie) {
        // Add this movie to purchased
        if (currentMember != null) {
            try {
                if (!con.isClosed()) {
                    PreparedStatement statement = con.prepareStatement(
                            "insert into purchase (member_id, movie_id, price)"
                            + " values (?, ?, 4.99)");
                    statement.setInt(1, currentMember.getMemberId());
                    statement.setInt(2, movie.getMovieId());
                    statement.execute();
                    
                    statement.close();
                }
            } catch (PSQLException ex) {
                // show an error message if a movie that the member wished
                // to purchase already exists in their collection of 
                // purchased movies
                ServerErrorMessage errorMessage = ex.getServerErrorMessage();
                if (errorMessage.getMessage().equals(
                        "duplicate key value violates unique constraint \"purchase_pkey\"")) {
                    JOptionPane.showMessageDialog(
                            null, 
                            "Cannot purchase an existing movie", 
                            "Duplicate Movie", 
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                System.err.println("Database error when adding"
                        + " movies to a member's purchased");
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Increment the watch count of a movie
     * @param movie the movie from either the table in the purchased screen
     *        or the table in the recently watched screen
     */
    public void watchMovie(Movie movie) {
        if (currentMember != null) {
            try {
                if (!con.isClosed()) {
                    // increment the watchcount of the selected movie
                    PreparedStatement statement = con.prepareStatement(
                            "update recent set"
                            + " watchcount = watchcount + 1"
                            + " where member_id=? and movie_id=?"
                    );
                    statement.setInt(1, currentMember.getMemberId());
                    statement.setInt(2, movie.getMovieId());
                    int updateResult = statement.executeUpdate();
                    
                    // if the selected movie did not exist yet in the 
                    // recently watched table, then add it
                    if (updateResult == 0) {
                        statement = con.prepareStatement(
                                "insert into recent (member_id, movie_id, watchcount)"
                                + " values (?, ?, 1)"
                        );
                        statement.setInt(1, currentMember.getMemberId());
                        statement.setInt(2, movie.getMovieId());
                        int insertResult = statement.executeUpdate();
                    }
                    
                    statement.close();
                }
            } catch (SQLException ex) {
                System.err.println("Database error when adjusting"
                        + " movies in a member's recent");
                ex.printStackTrace();
            }
        }
    }

    /**
     * Swap the ranks of two movies in a member's queue
     * @param currentMovie the selected movie that the member desires to update
     * @param otherMovie the movie either ahead of behind the selected movie
     *                   to swap ranks with
     */
    public void swapMovieRank(Movie currentMovie, Movie otherMovie) {
        if (currentMember != null) {
            try {
                if (!con.isClosed()) {
                    // use a virtual copy of the queue table in order to take 
                    // the rank of one movie in either the original or copy
                    // queue table and replace it with the other movie in
                    // either the original or copy queue table
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
    
    /**
     * Helper method to digest, or hash, a specified password
     * @param password the plaintext string to digest
     * @return 
     */
    private String digestPassword(String password) {
        String digestedPassword = "";
        
        // get the salt value from the properties file
        // and append it to the current password for
        // additional security
        password = password + prop.getProperty("db.salt");
        byte input[] = password.getBytes();
        
        try {
            // using the specified hash algorithm from the 
            // properties file, digest the password with the
            // appended salt and return the result in hexadecimal
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
     * Remove the selected movies from a member's queue
     * @param moviesSelected the selected movies from the table in the
     *                       member queue screen
     */
    public void removeMoviesFromQueue(Collection<Movie> moviesSelected) {
        if (currentMember != null) {
            try {
                if (!con.isClosed()) {
                    // delete each movie from the member's queue
                    PreparedStatement statement = null;
                    for (Movie m : moviesSelected) {
                        statement = con.prepareStatement(
                            "delete from queue where"
                            + " member_id = ? and"
                            + " movie_id = ?");
                        statement.setInt(1, currentMember.getMemberId());
                        statement.setInt(2, m.getMovieId());
                        int deleteResult = statement.executeUpdate();
                    }
                    
                    // re-order the rank of the existing movies
                    // in the member's queue
                    int newRank = 1;
                    for (Movie m : getQueueMovies()) {
                        statement = con.prepareStatement(
                                "update queue set rank = ?"
                                + " where member_id = ? and"
                                + " movie_id = ?");
                        statement.setInt(1, newRank);
                        statement.setInt(2, currentMember.getMemberId());
                        statement.setInt(3, m.getMovieId());
                        int updateResult = statement.executeUpdate();
                        newRank++;
                    }
                    
                    statement.close();
                }
            } catch (SQLException ex) {
                System.err.println("Database error trying to remove movies"
                        + " from a member's movie queue");
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Remove the selected movies from a member's collection of 
     * recently watched movies
     * @param moviesSelected the selected movies from the table in the
     *                       recently watched screen
     */
    public void removeMoviesFromRecent(Collection<Movie> moviesSelected) {
        if (currentMember != null) {
            try {
                if (!con.isClosed()) {
                    // delete each movie from the member's table of 
                    // recently watched movies
                    PreparedStatement statement = null;
                    for (Movie m : moviesSelected) {
                        statement = con.prepareStatement(
                            "delete from recent where"
                            + " member_id = ? and"
                            + " movie_id = ?");
                        statement.setInt(1, currentMember.getMemberId());
                        statement.setInt(2, m.getMovieId());
                        int deleteResult = statement.executeUpdate();
                    }
                    statement.close();
                }
            } catch (SQLException ex) {
                System.err.println("Database error trying to remove movies"
                        + " from a member's recent movies");
                ex.printStackTrace();
            }
        }
    }
}
