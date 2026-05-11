//package org.example;
//
//import org.example.models.Booking;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//
//public class BookingService {
//
//    HashMap<String, Booking> bookingsDb = new HashMap<>();
//
//    public List<Booking> getBookingsByUser(int userId) {
//        List<Booking> bookings = new ArrayList<>();
//        for (Booking booking: bookingsDb.values()) {
//            if (booking.getUserId() == userId) {
//                bookings.add(booking);
//            }
//        }
//        return bookings;
//    }
//
//    public List<Booking> getBookingsByRoomId(int roomId) {
//        List<Booking> bookings = new ArrayList<>();
//        for (Booking booking: bookingsDb.values()) {
//            if (booking.getRoomId() == roomId) {
//                bookings.add(booking);
//            }
//        }
//        return bookings;
//    }
//
//    public List<Booking> getBookingsByTime(int start, int end) {
//        List<Booking> bookings = new ArrayList<>();
//        for (Booking booking: bookingsDb.values()) {
//            if (isOverlap(booking, start, end)) {
//                bookings.add(booking);
//            }
//        }
//        return bookings;
//    }
//
//    public Booking getBookingById(String id) {
//        return bookingsDb.getOrDefault(id, null);
//    }
//
//    private boolean isOverlap(List<Booking> bookings, int start, int end) {
//        bookings.sort(Comparator.comparing(Booking::getStart));
//
//        for (Booking booking: bookings) {
//            if (booking.getEnd() < start) continue;
//            return !(booking.getStart() > end);
//        }
//        return false;
//    }
//
//    private boolean isOverlap(Booking booking, int start, int end) {
//        return booking.getEnd() >= start && booking.getStart() <= end;
//    }
//
//    public boolean addBooking(String bookingId, int userId, int roomId, int start, int end) {
//        // If room is available
//        List<Booking> bookingForRoomId = getBookingsByRoomId(roomId);
//        if (isOverlap(bookingForRoomId, start, end)) return false;
//        // If user is available
////        List<Booking> bookingForUserId = getBookingsByUser(userId);
////        if (isOverlap(bookingForUserId, start, end)) return false;
//        // we can add
//        bookingsDb.put(bookingId, new Booking(bookingId, roomId, userId, start, end));
//        return true;
//    }
//
//    public boolean cancel(String bookingId) {
//        bookingsDb.remove(bookingId);
//        return true;
//    }
//}
