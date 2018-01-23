package com.dev.jt14s.quickbite;

import java.io.Serializable;
import java.util.Comparator;

public class Restaurant implements Serializable {

    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private double priceLevel;
    private double rating;
    private boolean openStatus;
    private boolean favorited;

    public Restaurant() {
        this.favorited = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    void setAddress(String address) {
        this.address = address;
    }

    void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    void setPriceLevel(double price) {
        this.priceLevel = price;
    }

    void setRating(double rating) {
        this.rating = rating;
    }

    void setOpenStatus(String status) {
        this.openStatus = status.equals("true");
    }

    String getName() {
        return name;
    }

    String getAddress() {
        return address;
    }

    double getLatitude() {
        return latitude;
    }

    double getLongitude() {
        return longitude;
    }

    double getPriceLevel() {
        return priceLevel;
    }

    double getRating() {
        return rating;
    }

    boolean isOpenStatus() {
        return openStatus;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}
