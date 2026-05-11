package org.example;

import org.example.models.Ride;
import org.example.models.RideDistance;
import org.example.services.RiderService;
import org.example.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class CarpoolingService {

    UserService userService = new UserService();
    RiderService riderService = new RiderService();

    public String addUser(String userId) {
        if (userId.isBlank()) return "User Id invalid";
        return userService.registerUser(userId);
    }

    public String startRide(String userName, int seats, double startLat, double startLong, double endLat, double endLong) {
        if (userName.isBlank()) return "User name invalid";
        if (!userService.isUserRegistered(userName)) return "User not registered!";
        return riderService.startRide(userName, seats, startLat, startLong, endLat, endLong);
    }

    public List<String> findRide(String userName, double startLat, double startLong, double endLat, double endLong) {
//        System.out.println("userName " + userName);
        if (userName.isBlank()) return new ArrayList<>();
        if (!userService.isUserRegistered(userName)) return new ArrayList<>();
//        System.out.println("userName " + userName);
        List<RideDistance> rides = riderService.findRide(userName, startLat, startLong, endLat, endLong);
        List<Ride> selectedRides = new ArrayList<>();
//        for (RideDistance rideDistance: rides) {
//            selectedRides.add(rideDB.get(rideDistance.getDriverId()));
//        }
        List<String> result = new ArrayList<>();
        for (RideDistance rideDistance : rides) {
            String driverId = rideDistance.getDriverId();
            Ride ride = riderService.getRide(driverId);
            result.add(String.format("%s %s %s %sKM", ride.getRideId(), ride.getDriverId(), ride.getAvailableSeats(), rideDistance.getDistance()));
        };
        return result;
    }

    public String takeRide(String userName, String rideId) {
        return riderService.addRider(rideId, userName);
    }
}
