package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        GetInShape g = new GetInShape();

        // Onboard centers
        g.onboardCenter("connaught_place", List.of("6-9", "18-21"), List.of("weights", "cardio", "yoga", "swimming"));
        g.onboardCenter("bandra_west", List.of("7-10", "19-22"), List.of("weights", "cardio", "yoga"));

        // Add workout slots - connaught_place
        System.out.println(g.addWorkoutSlot("connaught_place", "weights", 6, 7, 100));   // true
        System.out.println(g.addWorkoutSlot("connaught_place", "cardio", 7, 8, 150));    // true
        System.out.println(g.addWorkoutSlot("connaught_place", "yoga", 8, 9, 200));      // true

        // Add workout slots - bandra_west
        System.out.println(g.addWorkoutSlot("bandra_west", "weights", 18, 19, 100));     // false (outside timings)
        System.out.println(g.addWorkoutSlot("bandra_west", "swimming", 19, 20, 100));    // false (swimming not allowed)
        System.out.println(g.addWorkoutSlot("bandra_west", "cardio", 19, 20, 20));       // true
        System.out.println(g.addWorkoutSlot("bandra_west", "weights", 20, 21, 100));     // true
        System.out.println(g.addWorkoutSlot("bandra_west", "weights", 21, 22, 100));     // true

        // View workout availability by start time
        System.out.println(g.viewWorkoutAvailabilityByStartTime("weights", "*"));
        // [connaught_place|weights|6|7|100, bandra_west|weights|20|21|100, bandra_west|weights|21|22|100]

        // Book session
        System.out.println(g.bookSession("vaibhav", "connaught_place", "weights", 6, 7));  // BOOKED

        // Verify seat count decreased
        System.out.println(g.viewWorkoutAvailabilityByStartTime("weights", "*"));
        // [connaught_place|weights|6|7|99, bandra_west|weights|20|21|100, bandra_west|weights|21|22|100]

        // Duplicate booking
        System.out.println(g.bookSession("vaibhav", "connaught_place", "weights", 6, 7));  // ALREADY_BOOKED

        // View by seats available
        System.out.println(g.viewWorkoutAvailabilityBySeatsAvailable("weights", "bandra_west"));
        // [bandra_west|weights|20|21|100, bandra_west|weights|21|22|100]

        // Cancel + Notify-Me Flow
        System.out.println(g.addWorkoutSlot("connaught_place", "yoga", 18, 19, 1));       // true

        System.out.println(g.bookSession("arjun", "connaught_place", "yoga", 18, 19));    // BOOKED
        System.out.println(g.bookSession("rohit", "connaught_place", "yoga", 18, 19));    // NO_SEATS

        System.out.println(g.addToInterestList("rohit", "connaught_place", "yoga", 18, 19)); // INTEREST_ADDED

        System.out.println(g.cancelSession("arjun", "connaught_place", "yoga", 18, 19)); // CANCELLED

        System.out.println(g.notifyInterestedUsers("connaught_place", "yoga", 18, 19));
        // [NOTIFY|rohit|connaught_place|yoga|18-19]

        System.out.println(g.viewWorkoutAvailabilityByStartTime("yoga", "connaught_place"));
        // [connaught_place|yoga|8|9|200, connaught_place|yoga|18|19|1]
    }
}
