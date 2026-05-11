package org.example;

public class Utils {

    public static int getDayFromString(String date) {
        String[] parts = date.split("-");
        return Integer.parseInt(parts[2]);
    }

    public static int getMonthFromString(String date) {
        String[] parts = date.split("-");
        return Integer.parseInt(parts[1]);
    }

    public static int getYearFromString(String date) {
        String[] parts = date.split("-");
        return Integer.parseInt(parts[0]);
    }

    public static boolean isOverlap(String existingStart, String existingEnd, String newStart, String newEnd) {
        int existingYear = getYearFromString(existingStart);
        int newYear = getYearFromString(newStart);
        if (existingYear != newYear) return false;
        int existingMonth = getMonthFromString(existingStart);
        int newMonth = getMonthFromString(newStart);
        if (existingMonth != newMonth) return false;
        return getDayFromString(existingEnd) >= getDayFromString(newStart) && getDayFromString(existingStart) <= getDayFromString(newEnd);
    }

    public static int getDaysBetweenDates(String end, String end2, String start) {
        int actualEnd = Math.max(getDayFromString(end), getDayFromString(end2));
        return 1 + actualEnd - getDayFromString(start);
    }
}
