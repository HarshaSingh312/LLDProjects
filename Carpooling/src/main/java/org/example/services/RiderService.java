package org.example.services;

import org.example.Util;
import org.example.models.Location;
import org.example.models.Ride;
import org.example.models.RideDistance;

import java.util.*;

public class RiderService {

    // driverId to Ride
    private HashMap<String, Ride> rideDB = new HashMap<>();

    private HashMap<String, String> rideToDriverMap = new HashMap<>();

    public String addRider(String rideId, String userId) {
        Ride ride = getRideFromRideId(rideId);
        if (Objects.isNull(ride)) return "Invalid rideId: " + rideId;
        ride.addRider(userId);
        return "Ride Confirmed!";
    }

    public Ride getRide(String driverId) {
        return rideDB.get(driverId);
    }

    public Ride getRideFromRideId(String rideId) {
        return rideDB.get(rideToDriverMap.get(rideId));
    }

    private String getRideId() {
        return UUID.randomUUID().toString();
    }

    public String startRide(String userName, int seats, double startLat, double startLong, double endLat, double endLong) {
        if (userName.isBlank()) return "Invalid user name";
        if (rideDB.containsKey(userName)) return "Ride already present";
        Ride ride = new Ride(getRideId(), userName, seats, new Location(startLat, startLong), new Location(endLat, endLong));
        rideDB.put(userName, ride);
        rideToDriverMap.put(ride.getRideId(), userName);
        return "Ride Started Successfully!";
    }

    public List<RideDistance> findRide(String userName, double startLat, double startLong, double endLat, double endLong) {
        if (userName.isBlank()) return new ArrayList<>();
        List<RideDistance> availableRides = new ArrayList<>();
        Location userStartLocation = new Location(startLat, startLong);
        Location userendLocation = new Location(endLat, endLong);
        for (Ride ride: rideDB.values()) {
            double distance = Util.getDistance(ride.getStartLocation(), userStartLocation);
            if (distance <= 5 && ride.isSeatAvailable()) {
                availableRides.add(new RideDistance(ride.getDriverId(), distance));
            }
        }
//        System.out.println("ridess" + availableRides);
        if (availableRides.isEmpty()) return new ArrayList<>();
        return availableRides.stream().sorted(Comparator.comparing(RideDistance::getDistance)).toList();
    }
}
