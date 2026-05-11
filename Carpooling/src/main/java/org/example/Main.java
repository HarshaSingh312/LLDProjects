package org.example;

import org.example.models.Request;
import org.example.services.TransformService;

import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        CarpoolingService carpoolingService = new CarpoolingService();
        TransformService transformService = new TransformService();
        List<String> requests = new ArrayList<>();
        String userRequest = "REGISTERUSER John";
        String userRequest2 = "REGISTERUSER Mike";
        String userRequest3 = "REGISTERUSER Alice";
        String giveRideRequest = "GIVERIDE John 4 12.9716 77.5946 12.9816 77.6946 0900";
        String giveRideRequest2 = "GIVERIDE Mike 6 12.9815 77.5932 12.9915 77.6932 0900";
        String findRideRequest = "FINDRIDE Alice 12.9716 77.5946 12.9816 77.6946";
        String request4 = "TAKERIDE Alice 1";

        requests.add(userRequest);
        requests.add(userRequest2);
        requests.add(userRequest3);
        requests.add(giveRideRequest);
        requests.add(giveRideRequest2);
        requests.add(findRideRequest);
        requests.add("TAKERIDE Alice e6369a44-bc4d-4993-8e21-bd33f0eb3c71");

        for (String request: requests) {
            Request transformed = transformService.transform(request);
            if (transformed.getOperations().equals("GIVERIDE")) {
                System.out.println(carpoolingService.startRide(transformed.getUserName(), transformed.getSeats(),
                        transformed.getStartLat(), transformed.getStartLon(), transformed.getEndLat(), transformed.getEndLon()));
            } else if (transformed.getOperations().equals("FINDRIDE")) {
                System.out.println(carpoolingService.findRide(transformed.getUserName(),
                        transformed.getStartLat(), transformed.getStartLon(), transformed.getEndLat(), transformed.getEndLon()));
            } else if (transformed.getOperations().equals("TAKERIDE")) {
                System.out.println(carpoolingService.takeRide(transformed.getUserName(), transformed.getRideId()));
            } else if (transformed.getOperations().equals("REGISTERUSER")) {
                System.out.println(carpoolingService.addUser(transformed.getUserName()));
            }
        }
    }
}
