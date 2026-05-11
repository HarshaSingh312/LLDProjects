package org.example.model;

public class InterestedUser {

    private final String userId;
    private final String centerName;
    private final String workoutType;
    private final Interval time;

    public InterestedUser(String userId, String centerName, String workoutType, int start, int end) {
        this.userId = userId;
        this.centerName = centerName;
        this.workoutType = workoutType;
        this.time = new Interval(start, end);
    }

    public String getUserId() {
        return userId;
    }

    public String getCenterName() {
        return centerName;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public Interval getTime() {
        return time;
    }
}
