package org.example.models;

public class Request {

    public Request(String operations, String userName, int seats, double startLat, double startLon, double endLat, double endLon, String rideId) {
        this.operations = operations;
        this.userName = userName;
        this.seats = seats;
        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon;
        this.rideId = rideId;
    }

    String operations;
    String userName;
    String rideId;
    int seats;
    double startLat;
    double startLon;
    double endLat;
    double endLon;

    public String getOperations() {
        return operations;
    }

    public String getUserName() {
        return userName;
    }

    public String getRideId() {
        return rideId;
    }

    public int getSeats() {
        return seats;
    }

    public double getStartLat() {
        return startLat;
    }

    public double getStartLon() {
        return startLon;
    }

    public double getEndLat() {
        return endLat;
    }

    public double getEndLon() {
        return endLon;
    }
}
