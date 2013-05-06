package edu.rit.cs.dbc.model;

import java.util.Collection;

/**
 * A bean representing the movie table in the database
 */
public class Movie {
    
    // the movie's identification number
    private int movieId;
    
    // the name of the movie
    private String title;
    
    // the suitability of the movie to audiences
    private String rating;
    
    // the genres that classify the movie
    private Collection<String> genre;
    
    // the year that the movie was released
    private int year;
    
    // how liked or disliked the movie is
    private float score;

    /**
     * Constructor to instantiate a structure for holding movie data
     * @param movieId the movie's identification number
     * @param title the name of the movie
     * @param rating the suitability of the movie to audiences
     * @param genre the genres that classify the movie
     * @param year the year that the movie was released
     * @param score how liked or disliked the movie is
     */
    public Movie(int movieId, String title, String rating, Collection<String> genre, int year, float score) {
        this.movieId = movieId;
        this.title = title;
        this.rating = rating;
        this.genre = genre;
        this.year = year;
        this.score = score;
    }

    /**
     * Getter for the movie's identification number
     * @return the movie's identification number
     */
    public int getMovieId() {
        return movieId;
    }

    /**
     * Setter for the movie's identification number
     * @param movieId the movie's identification number
     */
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    /**
     * Getter for the name of the movie
     * @return the name of the movie
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the name of the movie
     * @param title the name of the movie
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the suitability of the movie to audiences
     * @return the suitability of the movie to audiences
     */
    public String getRating() {
        return rating;
    }

    /**
     * Getter for the suitability of the movie to audiences
     * @param rating the suitability of the movie to audiences
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * Getter for the genres that classify the movie
     * @return the genres that classify the movie
     */
    public Collection<String> getGenres() {
        return genre;
    }

    /**
     * Setter for the genres that classify the movie
     * @param genre the genres that classify the movie
     */
    public void setGenres(Collection<String> genre) {
        this.genre = genre;
    }

    /**
     * Getter for the year that the movie was released
     * @return the year that the movie was released
     */
    public int getYear() {
        return year;
    }

    /**
     * Setter for the year that the movie was released
     * @param year the year that the movie was released
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Getter for how liked or disliked the movie is
     * @return how liked or disliked the movie is
     */
    public float getScore() {
        return score;
    }

    /**
     * Setter for how liked or disliked the movie is
     * @param score how liked or disliked the movie is
     */
    public void setScore(float score) {
        this.score = score;
    }
    
    /**
     * The representation of the movie in text
     * @return the movie's attributes
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(movieId);
        sb.append("; ");
        sb.append(title);
        sb.append("; ");
        sb.append(genre);
        sb.append("; ");
        sb.append(year);
        sb.append("; ");
        sb.append(rating);
        sb.append("; ");
        sb.append(score);
        return sb.toString();
    }

}