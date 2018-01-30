package com.mgfdev.elcaminodelacerveza.dto;

import com.mgfdev.elcaminodelacerveza.helpers.DateHelper;

import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Maxi on 29/10/2017.
 */

public class Passport {
    private User user;
    private List<PassportItem> brewers;

    public Passport(){
        brewers = new ArrayList<PassportItem>();
    }

    public void addBrewer(String brewer) {
        addBrewer(brewer, new Date());
    }


    public void addBrewer(String brewer, Date dt) {
        PassportItem item = findItemBy(brewer);
        if (item == null){
            item = new PassportItem(brewer, dt);
            brewers.add(item);
        }
        else{
            item.addVisit(new Date());
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PassportItem> getBrewers() {
        return brewers;
    }

    public void setBrewers(List<PassportItem> brewers) {
        this.brewers = brewers;
    }

    public PassportItem findItemBy(String brewerName) {
        PassportItem foundItem = null;
        for (PassportItem item : brewers) {
            if (brewerName.equalsIgnoreCase(item.getBrewerName())) {
                foundItem = item;
            }
        }
        return foundItem;
    }

    public boolean wasRegisteredToday(String brewerName) {
        for (PassportItem item : brewers) {
            if (brewerName.equalsIgnoreCase(item.getBrewerName()) && DateHelper.isSameWorkingDay(item.getLastVisit())) {
                return true;
            }
        }
        return false;
    }
}