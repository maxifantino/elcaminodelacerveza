package com.mgfdev.elcaminodelacerveza.dto;

import java.util.Date;

/**
 * Created by Maxi on 10/12/2017.
 */

public class Brewer {

    private String brewerName;
    private Date dateCreated;

    public String getBrewerName() {
        return brewerName;
    }

    public void setBrewerName(String brewerName) {
        this.brewerName = brewerName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }


    public Brewer (String name, Date createdDate){
        this.brewerName = name;
        this.dateCreated = createdDate;
    }
}
