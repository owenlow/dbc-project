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
public class Purchase {
    
    private double price;
    private Timestamp timestamp;
    private Movie movie;
    private Member member;
    
    public Purchase(double price, Timestamp timestamp, Movie movie, Member member) {
        this.price = price;
        this.timestamp = timestamp;
        this.movie = movie;
        this.member = member;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
