/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.model;

import java.sql.Timestamp;

/**
 *
 * @author ptr5201
 */
public class Recent {
    
    private int watchcount;
    private Timestamp timestamp;
    private Movie movie;
    private Member member;
    
    public Recent(int watchcount, Timestamp timestamp, Movie movie, Member member) {
        this.watchcount = watchcount;
        this.timestamp = timestamp;
        this.movie = movie;
        this.member = member;
    }

    public int getWatchcount() {
        return watchcount;
    }

    public void setWatchcount(int watchcount) {
        this.watchcount = watchcount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    
}
