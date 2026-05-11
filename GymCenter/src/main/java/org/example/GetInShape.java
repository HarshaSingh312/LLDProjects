package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class GetInShape {
    private HashMap<String, Center> centers = new HashMap<>();

    public GetInShape() {
    }

    public void onboardCenter(String centerName, List<String> centerTimings, List<String> workoutTypes) {
        if (centerName.isBlank()) {
            throw new IllegalArgumentException();
        }
        Center c = new Center(centerName, centerTimings, workoutTypes);
        centers.put(centerName, c);
    }

    public boolean addWorkoutSlot(String centerName, String workoutType, int startTime, int endTime, int totalSeats) {
        if (!centers.containsKey(centerName)) return false;
        if (endTime <= startTime || totalSeats <= 0) return false;
        Center c = centers.get(centerName);
        return c.addSlots(workoutType, startTime, endTime, totalSeats);
    }

    private List<Slot> viewWorkoutAvailability(String workoutType, String centerName) {
        List<Slot> slots = new ArrayList<>();
        if (centerName.isBlank() || "*".equals(centerName)) {
            for (String name: centers.keySet()) {
                slots.addAll(centers.get(name).getSlots(workoutType));
            }
        } else {
            if (!centers.containsKey(centerName)) return slots;
            slots = centers.get(centerName).getSlots(workoutType);
        }
        return slots;
    }

    private List<String> getFormatedSlots(List<Slot> slots) {
        List<String> result = new ArrayList<>();
        for (Slot s: slots) {
            result.add(String.format("%s|%s|%d|%d|%d", s.getCenter(), s.getWorkoutType(), s.getInterval().getStart(), s.getInterval().getEnd(), s.getSeats()));
        }
        return result;
    }

    public List<String> viewWorkoutAvailabilityByStartTime(String workoutType, String centerName) {
        List<Slot> slots = viewWorkoutAvailability(workoutType, centerName);
        slots.sort(Comparator.comparing((Slot slot) -> slot.getInterval().getStart()).thenComparing(Slot::getCenter));
        return getFormatedSlots(slots);
    }

    public List<String> viewWorkoutAvailabilityBySeatsAvailable(String workoutType, String centerName) {
        if (centerName.isBlank() || "*".equals(centerName)) return new ArrayList<>();
        List<Slot> slots = viewWorkoutAvailability(workoutType, centerName);
        slots.sort(Comparator.comparing(Slot::getSeats).thenComparing(Slot::getCenter));
        return getFormatedSlots(slots);
    }

    public String bookSession(String userId, String centerName, String workoutType, int startTime, int endTime) {
        Center c = centers.get(centerName);
        return c.book(userId, workoutType, startTime, endTime).name();
    }

    public String cancelSession(String userId, String centerName, String workoutType, int startTime, int endTime) {
        Center c = centers.get(centerName);
        return c.cancelBooking(userId, workoutType, startTime, endTime).name();
    }

    public String addToInterestList(String userId, String centerName, String workoutType, int startTime, int endTime) {
        Center c = centers.get(centerName);
        return c.addInterestedUsers(userId, workoutType, startTime, endTime);
    }

    public List<String> notifyInterestedUsers(String centerName, String workoutType, int startTime, int endTime) {
        Center c = centers.get(centerName);
        List<String> result = new ArrayList<>();
        for (var iu : c.getInterestedUsers(workoutType, startTime, endTime)) {
            result.add(String.format("NOTIFY|%s|%s|%s|%d-%d", iu.getUserId(), centerName, workoutType, startTime, endTime));
        }
        return result;
    }
}

enum BookingStatus {
    BOOKED,
    NO_SEATS,
    SLOT_NOT_FOUND,
    ALREADY_BOOKED,
    BOOKING_NOT_FOUND,
    CANCELLED
}

class Interval {
    private final int start;
    private final int end;

    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }
}

class Slot {
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

    public Interval getInterval() { return interval; }
    public String getWorkoutType() { return workoutType; }
    public int getSeats() { return seats; }
    public void reduceSeats() { this.seats = seats - 1; }
    public void addSeat() { this.seats = seats + 1; }
    public String getCenter() { return center; }
}

class Bookings {
    private final String userId;
    private final String centerName;
    private final String workoutType;
    private final Interval time;
    private BookingStatus status;

    public Bookings(String userId, String name, String workoutType, int start, int end) {
        this.userId = userId;
        this.centerName = name;
        this.workoutType = workoutType;
        this.time = new Interval(start, end);
    }

    public String getWorkoutType() { return workoutType; }
    public String getCenterName() { return centerName; }
    public String getUserId() { return userId; }
    public Interval getTime() { return time; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public BookingStatus getStatus() { return status; }
}

class InterestedUser {
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

    public String getUserId() { return userId; }
    public String getCenterName() { return centerName; }
    public String getWorkoutType() { return workoutType; }
    public Interval getTime() { return time; }
}

class Center {
    private final String name;
    private final List<Interval> centerTimings;
    private final List<String> workoutTypes;
    private final List<Slot> slots = new ArrayList<>();
    private List<Bookings> bookings = new ArrayList<>();
    private List<InterestedUser> interestedUsers = new ArrayList<>();

    public Center(String name, List<String> centerTimings, List<String> workoutTypes) {
        this.name = name;
        this.workoutTypes = workoutTypes;
        this.centerTimings = new ArrayList<>();
        for (String time: centerTimings) {
            String[] timeInParts = time.split("-");
            if (timeInParts.length < 2) throw new IllegalArgumentException("Invalid Time");
            this.centerTimings.add(new Interval(Integer.parseInt(timeInParts[0]), Integer.parseInt(timeInParts[1])));
        }
    }

    public List<Interval> getCenterTimings() { return centerTimings; }
    public List<String> getWorkoutTypes() { return workoutTypes; }
    public String getName() { return name; }

    private boolean canAddSlot(int start, int end) {
        boolean isWithinRange = false;
        for (Interval i: centerTimings) {
            if (i.getStart() <= start && i.getEnd() >= end) isWithinRange = true;
        }
        if (!isWithinRange) return false;

        slots.sort(Comparator.comparing(slot -> slot.getInterval().getStart()));
        for (Slot s: slots) {
            if (s.getInterval().getStart() >= end) return true;
            if (s.getInterval().getEnd() <= start) continue;
            return false;
        }
        return true;
    }

    public boolean addSlots(String workoutType, int startTime, int endTime, int totalSeats) {
        if (!workoutTypes.contains(workoutType)) return false;
        if (!canAddSlot(startTime, endTime)) return false;
        slots.add(new Slot(new Interval(startTime, endTime), workoutType, totalSeats, name));
        return true;
    }

    public List<Slot> getSlots() { return slots; }

    public List<Slot> getSlots(String workoutType) {
        List<Slot> result = new ArrayList<>();
        for (Slot slot : slots) {
            if (Objects.equals(slot.getWorkoutType(), workoutType)) result.add(slot);
        }
        return result;
    }

    public BookingStatus book(String userId, String workoutType, int start, int end) {
        if (!workoutTypes.contains(workoutType)) return BookingStatus.SLOT_NOT_FOUND;

        for (Slot s: slots) {
            if (s.getInterval().getStart() == start && s.getInterval().getEnd() == end && Objects.equals(s.getWorkoutType(), workoutType)) {
                for (Bookings bookings1: bookings) {
                    if (Objects.equals(bookings1.getUserId(), userId) &&
                            bookings1.getTime().getStart() == start &&
                            bookings1.getTime().getEnd() == end) {
                        return BookingStatus.ALREADY_BOOKED;
                    }
                }
                if (s.getSeats() >= 1) {
                    Bookings bookings1 = new Bookings(userId, name, workoutType, start, end);
                    bookings.add(bookings1);
                    bookings1.setStatus(BookingStatus.BOOKED);
                    s.reduceSeats();
                    return BookingStatus.BOOKED;
                }
                else return BookingStatus.NO_SEATS;
            }
        }
        return BookingStatus.SLOT_NOT_FOUND;
    }

    public String addInterestedUsers(String userId, String workoutType, int start, int end) {
        if (!workoutTypes.contains(workoutType)) return "SLOT_NOT_FOUND";

        for (Slot s: slots) {
            if (s.getInterval().getStart() == start && s.getInterval().getEnd() == end) {
                for (InterestedUser iu: interestedUsers) {
                    if (Objects.equals(iu.getUserId(), userId) &&
                            iu.getTime().getStart() == start &&
                            iu.getTime().getEnd() == end) {
                        return "ALREADY_INTERESTED";
                    }
                }
                if (s.getSeats() > 0) return "SEATS_AVAILABLE";
                interestedUsers.add(new InterestedUser(userId, name, workoutType, start, end));
                return "INTEREST_ADDED";
            }
        }
        return "SLOT_NOT_FOUND";
    }

    public List<InterestedUser> getInterestedUsers(String workoutType, int start, int end) {
        List<InterestedUser> matched = interestedUsers.stream()
                .filter(iu -> Objects.equals(iu.getWorkoutType(), workoutType) &&
                        iu.getTime().getStart() == start && iu.getTime().getEnd() == end)
                .collect(Collectors.toList());
        interestedUsers.removeAll(matched);
        return matched;
    }

    private Bookings findBooking(String userId, String workoutType, int start, int end) {
        for (Bookings bookings1: bookings) {
            if (Objects.equals(bookings1.getUserId(), userId) &&
                    bookings1.getTime().getStart() == start &&
                    bookings1.getTime().getEnd() == end && Objects.equals(bookings1.getWorkoutType(), workoutType)) {
                return bookings1;
            }
        }
        return null;
    }

    public BookingStatus cancelBooking(String userId, String workoutType, int start, int end) {
        Bookings bookings1 = findBooking(userId, workoutType, start, end);

        for (Slot s: slots) {
            if (s.getInterval().getStart() == start && s.getInterval().getEnd() == end && Objects.equals(workoutType, s.getWorkoutType())) {
                if (Objects.isNull(bookings1)) return BookingStatus.BOOKING_NOT_FOUND;
                else {
                    bookings1.setStatus(BookingStatus.CANCELLED);
                    s.addSeat();
                    return BookingStatus.CANCELLED;
                }
            }
        }
        return BookingStatus.SLOT_NOT_FOUND;
    }
}
