package com.mgfdev.elcaminodelacerveza.data;

/**
 * Created by Maxi on 09/11/2017.
 */

public class BeerLocation {
    private String brewery;
    private Double latitude;
    private Double longitude;
    private String address;

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
