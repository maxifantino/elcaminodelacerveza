package com.mgfdev.elcaminodelacerveza.data;

import java.io.Serializable;

/**
 * Created by Maxi on 09/11/2017.
 */

public class BrewerInfo implements Serializable{
    private String brewery;
    private Double latitude;
    private Double longitude;
    private String address;
    private String phone;

    private String facebook;
    private String instagram;
    private String twitter;
    private String content;
    private String detailImages;

    public BrewerInfo(){
        brewery = "";
        address = "";
        phone = "";
        facebook ="";
        instagram = "";
        twitter = "";
        content = "";
    }
    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(String detailImages) {
        this.detailImages = detailImages;
    }

    public String getBrewery() {
        return brewery;
    }

    public void setBrewery(String brewery) {
        this.brewery = brewery;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double lattitude) {
        this.latitude = lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
