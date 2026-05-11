package org.example.model;

public class Bookings {

    private final String userId;
    private final String centerName;
    private final String workoutType;
    private final Interval time;
    private BookingStatus status;

    public Bookings(String userId, String name, String workoutType, int start, int end) {
        this.userId  = userId;
        this.centerName = name;
        this.workoutType = workoutType;
        this.time = new Interval(start, end);
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public String getCenterName() {
        return centerName;
    }

    public String getUserId() {
        return userId;
    }

    public Interval getTime() {
        return time;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public BookingStatus getStatus() {
        return status;
    }
}
