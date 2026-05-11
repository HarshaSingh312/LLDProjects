// ****** It's better to write code in your local code editor and paste it back here *********

import java.util.*;
import java.util.concurrent.*;


public class Solution implements Q001ParkingLotInterface {
    private Helper01 helper;
    private String[][][] parking;
    private int floors=0;
    private ConcurrentHashMap<String, String> vehicleToSpot;
    private ConcurrentHashMap<String, String> ticketToSpot;
    private ConcurrentHashMap<String, String[]> spotInfo;
    public Solution(){}

    /**
     - use helper.print() and helper.println() for logging
       normal System.out.println() logs won't appear
     - parking[2][8][15] = parking spot at 2nd floor , 8th row and 15th column (0 based index),
       its spotId will be: "2-8-15"
     */
    public void init(Helper01 helper, String[][][] parking) {
        this.helper=helper;
        this.parking=parking;
        this.floors=parking.length;
        // add more initializations code here as you require
        vehicleToSpot = new ConcurrentHashMap<>();
        ticketToSpot = new ConcurrentHashMap<>();
        spotInfo = new ConcurrentHashMap<>();
        // helper.println("parking lot initialized");
    }

    /**
     * ParkingResult status 201 for success, 404 for error
     * vehicleType = 2 or 4 for 2-wheeler or 4-wheeler vehicle
     */
    public synchronized ParkingResult park(int vehicleType, String vehicleNumber, String ticketId){
        // use spotId=  helper.getSpotId(floor, row, column); to build spotId
        // or just do spotId = ""+floor+"-"+row+"-"+column
        for (int f = 0; f < floors; f++) {
            for (int r = 0; r < parking[f].length; r++) {
                for (int c = 0; c < parking[f][r].length; c++) {
                    String val = parking[f][r][c];
                    if (val == null) continue;
                    if (val.equals(vehicleType + "-1") || val.equals("" + vehicleType)) {
                        parking[f][r][c] = vehicleType + "-0";
                        String spotId = helper.getSpotId(f, r, c);
                        vehicleToSpot.put(vehicleNumber, spotId);
                        ticketToSpot.put(ticketId, spotId);
                        spotInfo.put(spotId, new String[]{vehicleNumber, ticketId, val});
                        return new ParkingResult(201, spotId, vehicleNumber, ticketId);
                    }
                }
            }
        }

        return new ParkingResult(404, "", vehicleNumber, ticketId);
        // return new ParkingResult(201, "0-2-16", vehicleNumber, ticketId);
    }

    /**
     * - returns 201 success, 404 : vehicle not found or any other error,
     * - exactly one of spotId, vehicleNumber or ticketId will be non-empty
     */
    public synchronized int removeVehicle(String spotId, String vehicleNumber, String ticketId){
        // extracting floor, row, column of parking spot where vehicle is parked
        // resolve spotId from whichever identifier is provided
        if (spotId == null || spotId.isEmpty()) {
            if (vehicleNumber != null && !vehicleNumber.isEmpty())
                spotId = vehicleToSpot.get(vehicleNumber);
            else if (ticketId != null && !ticketId.isEmpty())
                spotId = ticketToSpot.get(ticketId);
        }
        if (spotId == null || !spotInfo.containsKey(spotId)) return 404;

        String[] info = spotInfo.remove(spotId);
        vehicleToSpot.remove(info[0]);
        ticketToSpot.remove(info[1]);

        Integer []location=helper.getSpotLocation(spotId);
        int floor= location[0], row=location[1],column=location[2];
        // write code below to unpark the vehicle
        parking[floor][row][column] = info[2];

        // helper.print("vehicle removed");
        // return 201;
        return 201;
    }

    /** status = 200 : success, 404 : not found
     * exactly one of spotId, vehicleNumber or ticketId will be non-empty
     */
    public synchronized ParkingResult searchVehicle(String spotId, String vehicleNumber, String ticketId){
        if (spotId == null || spotId.isEmpty()) {
            if (vehicleNumber != null && !vehicleNumber.isEmpty())
                spotId = vehicleToSpot.get(vehicleNumber);
            else if (ticketId != null && !ticketId.isEmpty())
                spotId = ticketToSpot.get(ticketId);
        }
        if (spotId == null || !spotInfo.containsKey(spotId))
            return new ParkingResult(404, spotId != null ? spotId : "", vehicleNumber, ticketId);

        String[] info = spotInfo.get(spotId);
        return new ParkingResult(200, spotId, info[0], info[1]);
    }

    // floor is 0-index based, i.e.  0<=floor<parking.length
    public synchronized int getFreeSpotsCount(int floor, int vehicleType){
        int count = 0;
        for (int r = 0; r < parking[floor].length; r++)
            for (int c = 0; c < parking[floor][r].length; c++) {
                String val = parking[floor][r][c];
                if (val != null && (val.equals(vehicleType + "-1") || val.equals("" + vehicleType)))
                    count++;
            }
        return count;
    }
}
