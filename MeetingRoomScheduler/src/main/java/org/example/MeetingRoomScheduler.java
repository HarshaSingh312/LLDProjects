//package org.example;

import java.util.*;
import java.util.stream.Collectors;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

class Validator {

    public static boolean validateEmployeeId(int employee, int total) {
        if (employee < 0) return false;
        if (employee >= total) return false;
        return true;
    }

    public static boolean validateBookingRoomId(int id, int total) {
        if (id < 0) return false;
        if (id >= total) return false;
        return true;
    }

    public static boolean validateTimeRange(int start, int end) {
        if (start < 0 || end < 0) return false;
        if (start > end) return false;
        return true;
    }
}

class Booking {

    private String bookingId;
    private int roomId;
    private int userId;
    private int start;
    private int end;


    public Booking(String bookingId, int roomId, int userId, int start, int end) {
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.userId = userId;
        this.start = start;
        this.end = end;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}


class BookingService {

    HashMap<String, Booking> bookingsDb = new HashMap<>();

    public List<Booking> getBookingsByUser(int userId) {
        List<Booking> bookings = new ArrayList<>();
        for (Booking booking: bookingsDb.values()) {
            if (booking.getUserId() == userId) {
                bookings.add(booking);
            }
        }
        bookings.sort(Comparator.comparing(Booking::getStart).thenComparing(Booking::getBookingId));
        return bookings;
    }

    public List<Booking> getBookingsByRoomId(int roomId) {
        List<Booking> bookings = new ArrayList<>();
        for (Booking booking: bookingsDb.values()) {
            if (booking.getRoomId() == roomId) {
                bookings.add(booking);
            }
        }
        bookings.sort(Comparator.comparing(Booking::getStart).thenComparing(Booking::getBookingId));
        return bookings;
    }

    public List<Booking> getBookingsByTime(int start, int end) {
        List<Booking> bookings = new ArrayList<>();
        for (Booking booking: bookingsDb.values()) {
            if (isOverlap(booking, start, end)) {
                bookings.add(booking);
            }
        }
        bookings.sort(Comparator.comparing(Booking::getStart).thenComparing(Booking::getBookingId));
        return bookings;
    }

    public Booking getBookingById(String id) {
        return bookingsDb.getOrDefault(id, null);
    }

    private boolean isOverlap(List<Booking> bookings, int start, int end) {
        bookings.sort(Comparator.comparing(Booking::getStart));

        for (Booking booking: bookings) {
            if (booking.getEnd() < start) continue;
            return !(booking.getStart() > end);
        }
        return false;
    }

    private boolean isOverlap(Booking booking, int start, int end) {
        return booking.getEnd() >= start && booking.getStart() <= end;
    }

    public boolean addBooking(String bookingId, int userId, int roomId, int start, int end) {
        // If room is available
        List<Booking> bookingForRoomId = getBookingsByRoomId(roomId);
        if (isOverlap(bookingForRoomId, start, end)) return false;
        // If user is available
//        List<Booking> bookingForUserId = getBookingsByUser(userId);
//        if (isOverlap(bookingForUserId, start, end)) return false;
        // we can add
        bookingsDb.put(bookingId, new Booking(bookingId, roomId, userId, start, end));
        return true;
    }

    public boolean cancel(String bookingId) {
        bookingsDb.remove(bookingId);
        return true;
    }
}


public class MeetingRoomScheduler {

    BookingService service = new BookingService();
    private final int roomCount;
    private final int employeeCount;

    public MeetingRoomScheduler(int roomsCount, int employeesCount) {
        this.roomCount = roomsCount;
        this.employeeCount = employeesCount;
    }

    public boolean bookRoom(String bookingId, int employeeId, int roomId, int startTime, int endTime) {
        if (!Validator.validateEmployeeId(employeeId, employeeCount)) return false;
        if (!Validator.validateBookingRoomId(roomId, roomCount)) return false;
        if (!Validator.validateTimeRange(startTime, endTime)) return false;
        return service.addBooking(bookingId, employeeId, roomId, startTime, endTime);
    }

    public List<Integer> getAvailableRooms(int startTime, int endTime) {
        if (startTime > endTime) return new ArrayList<>();
        List<Booking> bookings = service.getBookingsByTime(startTime, endTime);
        List<Integer> availableRooms = new ArrayList<>();
        for (int i = 0; i < roomCount; i++) {
            final int index = i;
            if (bookings.stream().noneMatch(booking -> booking.getRoomId() == index)) {
                availableRooms.add(i);
            }
        }
        return availableRooms;
    }

    public boolean cancelBooking(String bookingId) {
        if (Objects.isNull(service.getBookingById(bookingId))) return false;
        return service.cancel(bookingId);
    }

    public List<String> listBookingsForRoom(int roomId) {
        return service.getBookingsByRoomId(roomId).stream().map(Booking::getBookingId).collect(Collectors.toList());
    }

    public List<String> listBookingsForEmployee(int employeeId) {
        return service.getBookingsByUser(employeeId).stream().map(Booking::getBookingId).collect(Collectors.toList());
    }
}