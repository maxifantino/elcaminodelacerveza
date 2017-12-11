package com.mgfdev.elcaminodelacerveza.dto;

/**
 * Created by Maxi on 10/12/2017.
 */

public class BrewerListItem {

    private String brewerName;
    private String brewerImage;
    private String brewerDescription;
    private String address;

    public BrewerListItem (String name, String imageUrl, String description, String address){
        this.brewerName = name;
        this.brewerImage = imageUrl;
        this.brewerDescription = description;
        this.address = address;
    }

    public String getBrewerName() {
        return brewerName;
    }

    public void setBrewerName(String brewerName) {
        this.brewerName = brewerName;
    }

    public String getBrewerImage() {
        return brewerImage;
    }

    public void setBrewerImage(String brewerImage) {
        this.brewerImage = brewerImage;
    }

    public String getBrewerDescription() {
        return brewerDescription;
    }

    public void setBrewerDescription(String brewerDescription) {
        this.brewerDescription = brewerDescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
