/*
 * Contributors: Owen Royall-Kahin, Philip Rodriguez, Patrick Duffy
 */
package edu.rit.cs.dbc.model;

import java.sql.Timestamp;

/**
 * A bean representing the purchase table in the database
 */
public class Purchase {
    
    // the price charged for the movie
    private double price;
    
    // the time that the movie was purchased
    private Timestamp timestamp;
    
    // the movie that was purchased
    private Movie movie;
    
    // the member who purchased the movie
    private Member member;
    
    /**
     * Constructor to instantiate a structure for holding purchase data
     * @param price the price charged for the movie
     * @param timestamp the time that the movie was purchased
     * @param movie the movie that was purchased
     * @param member the member who purchased the movie
     */
    public Purchase(double price, Timestamp timestamp, Movie movie, Member member) {
        this.price = price;
        this.timestamp = timestamp;
        this.movie = movie;
        this.member = member;
    }

    /**
     * Getter for the price charged for the movie
     * @return the price charged for the movie
     */
    public double getPrice() {
        return price;
    }

    /**
     * Setter for the price charged for the movie
     * @param price the price charged for the movie
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Getter for the time that the movie was purchased
     * @return the time that the movie was purchased
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Setter for the time that the movie was purchased
     * @param timestamp the time that the movie was purchased
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Getter for the movie that was purchased
     * @return the movie that was purchased
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Setter for the movie that was purchased
     * @param movie the movie that was purchased
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    /**
     * Getter for the member who purchased the movie
     * @return the member who purchased the movie
     */
    public Member getMember() {
        return member;
    }

    /**
     * Setter for the member who purchased the movie
     * @param member the member who purchased the movie
     */
    public void setMember(Member member) {
        this.member = member;
    }
    
}
