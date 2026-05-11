package org.example.models;

public class RideDistance {
    private String driverId;
    private double distance;

    public RideDistance(String rideId, double distance) {
        this.driverId = rideId;
        this.distance = distance;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
