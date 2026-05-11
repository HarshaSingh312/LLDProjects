package org.example;

import java.util.ArrayList;
import java.util.List;

public class Car {
    private String licensePlate;
    private int costPerDay;
    private int freeKmsPerDay;
    private int costPerKm;

    public List<String> getBookings() {
        return bookings;
    }

    public void addBookings(String bookingId) {
        this.bookings.add(bookingId);
    }

    public void removeBookings(String bookingId) {
        this.bookings.remove(bookingId);
    }

    private List<String> bookings = new ArrayList<>();

    public int getCarCost(int kms, int days) {
        int freeAllowance = days * freeKmsPerDay;
        int extraKms = Math.max(0, kms - freeAllowance);
        return extraKms * costPerKm + days * costPerDay;
    }

    public Car(String licensePlate, int costPerDay, int freeKmsPerDay, int costPerKm) {
        this.licensePlate = licensePlate;
        this.costPerDay = costPerDay;
        this.freeKmsPerDay = freeKmsPerDay;
        this.costPerKm = costPerKm;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getCostPerDay() {
        return costPerDay;
    }

    public void setCostPerDay(int costPerDay) {
        this.costPerDay = costPerDay;
    }

    public int getFreeKmsPerDay() {
        return freeKmsPerDay;
    }

    public void setFreeKmsPerDay(int freeKmsPerDay) {
        this.freeKmsPerDay = freeKmsPerDay;
    }

    public int getCostPerKm() {
        return costPerKm;
    }

    public void setCostPerKm(int costPerKm) {
        this.costPerKm = costPerKm;
    }
}
