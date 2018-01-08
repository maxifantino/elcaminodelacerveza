package com.mgfdev.elcaminodelacerveza.dto;

import java.util.Date;

/**
 * Created by Maxi on 02/01/2018.
 */

public class PassportItemDto {

    private Integer userId;
    private String brewerName;
    private String username;

    private String dateVisit;
    private Integer passportItemId;
    private String sincronized;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSincronized() {
        return sincronized;
    }

    public void setSincronized(String sincronized) {
        this.sincronized = sincronized;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBrewerName() {
        return brewerName;
    }

    public void setBrewerName(String brewerName) {
        this.brewerName = brewerName;
    }

    public String getDateVisit() {
        return dateVisit;
    }

    public void setDateVisit(String dateVisit) {
        this.dateVisit = dateVisit;
    }

    public Integer getPassportItem() {
        return passportItemId;
    }

    public void setPassportItem(Integer passportItem) {
        this.passportItemId = passportItem;
    }
}
