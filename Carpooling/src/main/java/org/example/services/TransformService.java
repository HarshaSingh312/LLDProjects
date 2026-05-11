package org.example.services;

import org.example.models.Request;


import java.util.ArrayList;
import java.util.List;

public class TransformService {
    List<String> validOperations = new ArrayList<>();

    public TransformService() {
        validOperations.add("GIVERIDE");
        validOperations.add("FINDRIDE");
        validOperations.add("TAKERIDE");
        validOperations.add("REGISTERUSER");
    }

    public Request transform(String s) {
        String[] parts = s.split(" ");
        String operation = parts[0];
        if (!validOperations.contains(operation)) throw new IllegalArgumentException("Invalid operations");
        String userId = parts[1];
        switch(operation) {
            case "GIVERIDE": {
                try {
                    if (parts.length < 7) throw new IllegalArgumentException("Invalid operations");
                    int seats = Integer.parseInt(parts[2]);
                    double startLat = Double.parseDouble(parts[3]);
                    double startLon = Double.parseDouble(parts[4]);
                    double endLat = Double.parseDouble(parts[5]);
                    double endLon = Double.parseDouble(parts[6]);
                    return new Request(operation, userId, seats, startLat, startLon, endLat, endLon, null);
                } catch (Exception exception) {
                    throw new IllegalArgumentException("Invalid operations");
                }
            } case "FINDRIDE": {
                try {
                    if (parts.length < 6) throw new IllegalArgumentException("Invalid operations");
                    double startLat = Double.parseDouble(parts[2]);
                    double startLon = Double.parseDouble(parts[3]);
                    double endLat = Double.parseDouble(parts[4]);
                    double endLon = Double.parseDouble(parts[5]);
                    return new Request(operation, userId, 0, startLat, startLon, endLat, endLon, null);
                } catch (Exception exception) {
                    throw new IllegalArgumentException("Invalid operations");
                }
            } case "TAKERIDE": {
                try {
                    if (parts.length < 3) throw new IllegalArgumentException("Invalid operations");
                    String rideId = parts[2];
                    return new Request(operation, userId, 0, 0, 0, 0, 0, rideId);
                } catch (Exception exception) {
                    throw new IllegalArgumentException("Invalid operations");
                }
            } case "REGISTERUSER": {
                return new Request(operation, userId, 0, 0, 0, 0, 0, null);
            } default: throw new IllegalArgumentException("No operation defined!");
        }

    }
}
