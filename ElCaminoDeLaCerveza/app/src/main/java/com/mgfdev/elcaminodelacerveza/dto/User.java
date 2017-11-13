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
        this.username = username;
    }
}
