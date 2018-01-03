package com.mgfdev.elcaminodelacerveza.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Maxi on 30/12/2017.
 */

public class PassportItem {

    private String brewerName;
    private List<Date> dateVisits;

    public PassportItem(String brewerName, Date dt){
        this.brewerName = brewerName;
        this.dateVisits = new ArrayList<Date> ();
        this.dateVisits.add(dt);
    }

    public String getBrewerName() {
        return brewerName;
    }

    public void setBrewerName(String brewerName) {
        this.brewerName = brewerName;
    }


    public boolean wasFirstVisit (){
        return dateVisits.size() == 1;
    }

    public void addVisit (Date dt){
        dateVisits.add(dt);
    }

    public Integer getVisitsCount(){
        return dateVisits.size();
    }

    public Date getLastVisit(){
        Date lastVisit = null;
        if (dateVisits.size() > 0){
            lastVisit = dateVisits.get(dateVisits.size() -1);
        }
      return lastVisit;
    }
}

