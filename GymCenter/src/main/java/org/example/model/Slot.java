package org.example.model;

public class Slot {

    private final Interval interval;
    private final String workoutType;
    private int seats;
    private final String center;

    public Slot(Interval interval, String workoutType, int seats, String centerName) {
        this.interval = interval;
        this.workoutType = workoutType;
        this.seats = seats;
        this.center = centerName;
    }

    public Interval getInterval() {
        return interval;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public int getSeats() {
        return seats;
    }

    public void reduceSeats() {
        this.seats = seats - 1;
    }

    public void addSeat() {
        this.seats = seats + 1;
    }

    public String getCenter() {
        return center;
    }
}
