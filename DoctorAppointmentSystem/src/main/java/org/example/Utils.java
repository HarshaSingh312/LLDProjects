package org.example;

import org.example.model.Slot;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Utils {

    public static int getTimeInMins(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int min = Integer.parseInt(parts[1]);
        return hour * 60 + min;
    }

    public static List<Slot> getSlotsFromString(List<String> slots, String doctorName) {
        return slots.stream().map(slot -> {
            String[] parts = slot.split("-");
            return new Slot(getTimeInMins(parts[0]), getTimeInMins(parts[1]), doctorName);
        }).collect(Collectors.toList());
    }

    public static String getTimingString(int timeInMins) {
        int hours = timeInMins / 60;
        int minutes = timeInMins % 60;
        String hoursS = hours < 10 ? "0" + hours : String.valueOf(hours);
        String minutesS = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
        return hoursS + ":" + minutesS;
    }

}
