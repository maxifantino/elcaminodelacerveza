package com.mgfdev.elcaminodelacerveza.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Maxi on 29/10/2017.
 */

public class Passport {
    private User user;
    private Map<String, Date> brewers;

    public void addBrewer(String brewer){
        if (brewers.get(brewer) == null){
            brewers.put(brewer, new Date());
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<String, Date> getBrewers() {
        return brewers;
    }

    public void setBrewers(Map<String, Date> brewers) {
        this.brewers = brewers;
    }
}
