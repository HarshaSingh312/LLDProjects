package org.example;

public class Booking {

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
