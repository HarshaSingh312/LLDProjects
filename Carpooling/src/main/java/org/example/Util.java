package org.example;

import org.example.models.Location;

public class Util {

    public static double getDistance(Location start, Location end) {
        double latDiff = end.getLat() - start.getLat();
        double lonDiff = end.getLon() - start.getLon();

        return Math.sqrt(Math.pow(latDiff, 2) + Math.pow(lonDiff, 2) * 111);
    }
}
