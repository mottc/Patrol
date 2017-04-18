package com.mottc.patrol.data.entity;

/**
 * Created with Android Studio
 * User: mottc
 * Date: 2017/4/18
 * Time: 21:25
 */
public class PatrolLocation {
    private double latitude;
    private double longitude;

    public PatrolLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
