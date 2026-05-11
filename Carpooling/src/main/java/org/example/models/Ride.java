package org.example.models;

import java.util.ArrayList;
import java.util.List;

public class Ride {
    public String getRideId() {
        return rideId;
    }

    private String rideId;

    public String getDriverId() {
        return driverId;
    }

    private String driverId;
    private List<String> riderIds = new ArrayList<>();
    private int totalSeats;

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    private Location startLocation;
    private Location endLocation;


    public Ride(String rideId, String driverId, int seats, Location startLocation, Location endLocation) {
        this.rideId = rideId;
        this.driverId = driverId;
        this.totalSeats = seats;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    public void addRider(String userId) {
        riderIds.add(userId);
    }

    public boolean isSeatAvailable() {
        return riderIds.size() < this.totalSeats;
    }

    public int getAvailableSeats() {
        return this.totalSeats - this.riderIds.size();
    }

}
