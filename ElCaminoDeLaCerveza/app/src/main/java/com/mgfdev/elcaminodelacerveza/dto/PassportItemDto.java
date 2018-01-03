package com.mgfdev.elcaminodelacerveza.dto;

import java.util.Date;

/**
 * Created by Maxi on 02/01/2018.
 */

public class PassportItemDto {

    private Integer userId;
    private String brewerName;
    private Date visitDate;
    private Integer passportItemId;
    private String sincronized;

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

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public Integer getPassportItem() {
        return passportItemId;
    }

    public void setPassportItem(Integer passportItem) {
        this.passportItemId = passportItem;
    }
}
