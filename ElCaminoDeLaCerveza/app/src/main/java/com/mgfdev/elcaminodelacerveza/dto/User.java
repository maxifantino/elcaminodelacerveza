package com.mgfdev.elcaminodelacerveza.dto;

import java.io.Serializable;

/**
 * Created by Maxi on 29/10/2017.
 */

public class User implements Serializable {

    private int id;
    private String username;
    private String password;
    private Passport passport;
    private String email;
    public User (){}

    public User (String username, String email, String password) {
         this(username, email, password, null);
    }

    public User (String username, String email, String password, Passport passport){
        this.username = username;
        this.password = password;
        this.email = email;
        this.passport = passport;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
