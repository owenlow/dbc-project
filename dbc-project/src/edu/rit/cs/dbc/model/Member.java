/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.model;

/**
 * A bean representing the member table in the database
 */
public class Member {
    
    // the member's identification number
    private int memberId;
    
    // the member's full name
    private String fullName;
    
    // the member's username
    private String username;
    
    /**
     * Constructor to instantiate a structure for holding member data
     * @param memberId the member's identification number
     * @param fullName the member's full name
     * @param username the member's username
     */
    public Member(int memberId, String fullName, String username) {
        this.memberId = memberId;
        this.fullName = fullName;
        this.username = username;
    }

    /**
     * Getter for the member's identification number
     * @return member's id
     */
    public int getMemberId() {
        return memberId;
    }

    /**
     * Setter for the member's identification number
     * @param memberId member's id
     */
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    /**
     * Getter for the member's full name
     * @return member's full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Setter for the member's full name
     * @param memberId member's full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Getter for the member's username
     * @return member's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the member's username
     * @param memberId member's username
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
