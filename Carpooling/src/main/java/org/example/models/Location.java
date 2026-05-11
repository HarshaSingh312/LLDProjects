package org.example.models;

public class Location {
    private double lat;
    private double lon;

    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public boolean equal(Location newLocation) {
        if (this.lat != newLocation.getLat()) return false;
        if (this.lon != newLocation.getLon()) return false;
        return true;
    }
}
