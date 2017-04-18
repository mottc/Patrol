package com.mottc.patrol.data.entity;

/**
 * Created with Android Studio
 * User: mottc
 * Date: 2017/4/18
 * Time: 21:25
 */
public class PatrolLocation {
    private double altitude;
    private double latitude;

    public PatrolLocation(double altitude, double latitude) {
        this.altitude = altitude;
        this.latitude = latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
