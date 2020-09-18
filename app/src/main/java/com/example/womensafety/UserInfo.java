package com.example.womensafety;

public class UserInfo {
    String email;
    double latitude, longitude;
    long lastUpdated;

    UserInfo() {

    }

    public UserInfo(String email, double latitude, double longitude, long lastUpdated) {
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastUpdated = lastUpdated;
    }

    public String getEmail() {
        return email;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }
}
