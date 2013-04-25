package edu.rit.cs.dbc.model;

import java.util.Collection;

/**
 *
 * @author pjd2424
 */
public class Movie {
    private String title, rating;
    private Collection<String> genre;
    private int year, movieId;
    private float score;

    public Movie(String title, String rating, Collection<String> genre, int year, int movieId, float score) {
        this.title = title;
        this.rating = rating;
        this.genre = genre;
        this.year = year;
        this.movieId = movieId;
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Collection<String> getGenres() {
        return genre;
    }

    public void setGenres(Collection<String> genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
    
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