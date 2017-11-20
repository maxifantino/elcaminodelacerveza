package com.mgfdev.elcaminodelacerveza.data;

/**
 * Created by Maxi on 09/11/2017.
 */

public class BeerLocation {
    private String craftName;
    private Double lattitude;
    private Double longitude;
    private String address;

    public String getCraftName() {
        return craftName;
    }

    public void setCraftName(String craftName) {
        this.craftName = craftName;
    }

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
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
