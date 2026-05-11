package org.example;

import java.util.HashMap;
import java.util.List;


//import static org.example.Utils.getDaysBetweenDates;
//import static org.example.Utils.isOverlap;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


class Utils {

    public static boolean isOverlap(String existingStart, String existingEnd, String newStart, String newEnd) {
        LocalDate eS = LocalDate.parse(existingStart), eE = LocalDate.parse(existingEnd);
        LocalDate nS = LocalDate.parse(newStart), nE = LocalDate.parse(newEnd);
        return !eE.isBefore(nS) && !nE.isBefore(eS);
    }

    public static int getDaysBetweenDates(String end, String end2, String start) {
        LocalDate e1 = LocalDate.parse(end), e2 = LocalDate.parse(end2), s = LocalDate.parse(start);
        LocalDate actualEnd = e1.isAfter(e2) ? e1 : e2;
        return (int) ChronoUnit.DAYS.between(s, actualEnd) + 1;
    }
}

class Car {
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


class Booking {

    private String orderId;
    private String carId;
    private String start;
    private String end;

    public int getInitalReading() {
        return initalReading;
    }

    public void setInitalReading(int initalReading) {
        this.initalReading = initalReading;
    }

    private int initalReading = 0;

    public Booking(String orderId, String carId, String start, String end) {
        this.orderId = orderId;
        this.carId = carId;
        this.start = start;
        this.end = end;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}

public class CarRentalService {

    HashMap<String, Car> carIds = new HashMap<>();
    HashMap<String, Booking> bookings = new HashMap<>();

    public CarRentalService() {
    }

    public void addCar(String licensePlate, int costPerDay, int freeKmsPerDay, int costPerKm) {
        if (carIds.containsKey(licensePlate)) return;
        if (costPerDay < 0 || freeKmsPerDay < 0 || costPerKm < 0) return;
        if (licensePlate.isBlank()) return;
        carIds.put(licensePlate, new Car(licensePlate, costPerDay, freeKmsPerDay, costPerKm));
    }

    public boolean bookCar(String orderId, String carLicensePlate, String fromDate, String tillDate) {
        if (bookings.containsKey(orderId)) return false;
        if (!carIds.containsKey(carLicensePlate)) return false;
        Car car = carIds.get(carLicensePlate);
        // Check car existing booking
        List<String> bookings = car.getBookings();
        for (String bookingId: bookings) {
            Booking b = this.bookings.get(bookingId);
            if (Utils.isOverlap(b.getStart(), b.getEnd(), fromDate, tillDate)) return false;
        }
        this.bookings.put(orderId, new Booking(orderId, carLicensePlate, fromDate, tillDate));
        car.addBookings(orderId);
        return true;
    }

    public void startTrip(String orderId, int odometerReading) {
        if (!bookings.containsKey(orderId)) return;
        Booking booking = bookings.get(orderId);
        booking.setInitalReading(odometerReading);
        bookings.put(orderId, booking);
    }

    public int endTrip(String orderId, int finalOdometerReading, String endDate) {
        if (bookings.containsKey(orderId)) {
            Booking booking = bookings.get(orderId);
            int totalKms = finalOdometerReading - booking.getInitalReading();
            int totalDays = Utils.getDaysBetweenDates(endDate, booking.getEnd(), booking.getStart());
//            System.out.println("totalKms " + totalKms);
//            System.out.println("totalDays " + totalDays);
            String carId = booking.getCarId();
            Car car = carIds.get(carId);
            int cost = car.getCarCost(totalKms, totalDays);
            booking.setEnd(endDate);
//            car.removeBookings(orderId);
            return cost;
        }
        return -1;
    }
}