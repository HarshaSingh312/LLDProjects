package org.example.model;

import java.util.*;

public class Center {
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

    public List<Interval> getCenterTimings() {
        return centerTimings;
    }

    public List<String> getWorkoutTypes() {
        return workoutTypes;
    }

    public String getName() {
        return name;
    }

    private boolean canAddSlot(int start, int end) {
        boolean isWithinRange = false;
        for (Interval i: centerTimings) {
            if (i.getStart() <= start && i.getEnd() >= end) isWithinRange = true;
        }
        if (!isWithinRange) return false;

        // sort based on start
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

    public List<Slot> getSlots() {
        return slots;
    }

    public List<Slot> getSlots(String workoutType) {
        return slots.stream().filter(slot -> Objects.equals(slot.getWorkoutType(), workoutType) && slot.getSeats() > 0).toList();
    }

    public BookingStatus book(String userId, String workoutType, int start, int end) {
        if (!workoutTypes.contains(workoutType)) return BookingStatus.SLOT_NOT_FOUND;

        for (Slot s: slots) {
            if (s.getInterval().getStart() == start && s.getInterval().getEnd() == end) {
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
        return interestedUsers.stream()
                .filter(iu -> Objects.equals(iu.getWorkoutType(), workoutType) &&
                        iu.getTime().getStart() == start && iu.getTime().getEnd() == end)
                .toList();
    }


    private Bookings findBooking(String userId, String workoutType, int start, int end) {
        for (Bookings bookings1: bookings) {
//            System.out.println(bookings1.getUserId() + ", " + bookings1.getWorkoutType() + ", " +
//                    bookings1.getTime().getStart() + ", " + bookings1.getTime().getEnd());
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
        if (Objects.isNull(bookings1)) return BookingStatus.BOOKING_NOT_FOUND;

        for (Slot s: slots) {
            if (s.getInterval().getStart() == start && s.getInterval().getEnd() == end) {
                bookings1.setStatus(BookingStatus.CANCELLED);
                s.addSeat();
//                if (s.getSeats() == 1) {
//                    getInterestedUsers();
//                }
                return BookingStatus.CANCELLED;
            }
        }
        return BookingStatus.SLOT_NOT_FOUND;
    }
}