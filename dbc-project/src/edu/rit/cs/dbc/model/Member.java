/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rit.cs.dbc.model;

/**
 *
 * @author ptr5201
 */
public class Member {
    private int memberId;
    private String fullName;
    private String username;
    
    public Member(int memberId, String fullName, String username) {
        this.memberId = memberId;
        this.fullName = fullName;
        this.username = username;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
