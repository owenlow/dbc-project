/*
 * Contributors: Owen Royall-Kahin, Philip Rodriguez, Patrick Duffy
 */
package edu.rit.cs.dbc.model;

import java.sql.Timestamp;

/**
 * A bean representing the recent table in the database
 */
public class Recent {
    
    // the number of times that the movie was watched
    private int watchcount;
    
    // the time that the movie was watched
    private Timestamp timestamp;
    
    // the movie that was recently watched
    private Movie movie;
    
    // the member who watched the movie
    private Member member;
    
    /**
     * Constructor to instantiate a structure for holding recently watched data
     * @param watchcount the number of times that the movie was watched
     * @param timestamp the time that the movie was watched
     * @param movie the movie that was recently watched
     * @param member the member who watched the movie
     */
    public Recent(int watchcount, Timestamp timestamp, Movie movie, Member member) {
        this.watchcount = watchcount;
        this.timestamp = timestamp;
        this.movie = movie;
        this.member = member;
    }

    /**
     * Getter for the number of times that the movie was watched
     * @return the number of times that the movie was watched
     */
    public int getWatchcount() {
        return watchcount;
    }

    /**
     * Setter for the number of times that the movie was watched
     * @param watchcount the number of times that the movie was watched
     */
    public void setWatchcount(int watchcount) {
        this.watchcount = watchcount;
    }

    /**
     * Getter for the time that the movie was watched
     * @return the time that the movie was watched
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Setter for the time that the movie was watched
     * @param timestamp the time that the movie was watched
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Getter for the movie that was recently watched
     * @return the movie that was recently watched
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Setter for the movie that was recently watched
     * @param movie the movie that was recently watched
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    /**
     * Getter for the member who watched the movie
     * @return the member who watched the movie
     */
    public Member getMember() {
        return member;
    }

    /**
     * Setter for the member who watched the movie
     * @param member the member who watched the movie
     */
    public void setMember(Member member) {
        this.member = member;
    }
    
}
