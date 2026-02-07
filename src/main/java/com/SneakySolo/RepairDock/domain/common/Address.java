package com.SneakySolo.RepairDock.domain.common;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String city;
    private String state;
    private String country;
    private String pincode;
    private Double latitude;
    private Double longitude;

    public Address() {
    }

    public Address(String city, Double longitude, Double latitude, String pincode, String country, String state) {
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
        this.pincode = pincode;
        this.country = country;
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
