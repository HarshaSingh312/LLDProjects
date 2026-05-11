package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
//        MeetingRoomScheduler s = new MeetingRoomScheduler(2, 2);
//        System.out.println(s.getAvailableRooms(10, 20));
////                => [0, 1]
//
//        System.out.println(s.bookRoom("b1", 0, 0, 10, 20));
////                => true
//
//        System.out.println(s.bookRoom("b2", 1, 0, 15, 25));
////                => false   // same room overlap
//
//        System.out.println(s.bookRoom("b3", 1, 0, 20, 30));
////                => false   // boundary overlaps because intervals are CLOSED (time=20 is shared)
//
//        System.out.println(s.bookRoom("b4", 1, 0, 21, 30));
////                => true    // no overlap with [10,20]
//
//        System.out.println(s.bookRoom("b5", 0, 1, 15, 25));
////                => true    // allowed: different room can overlap in time
//
//        System.out.println(s.getAvailableRooms(20, 20));
////                => []      // at time=20: room0 busy (b1), room1 busy (b5)
//
//        System.out.println(s.listBookingsForRoom(0));
////                => ["b1", "b4"]
//
//        System.out.println(s.listBookingsForRoom(1));
////                => ["b5"]
//
//        System.out.println(s.listBookingsForEmployee(0));
////                => ["b1", "b5"]
//
//        System.out.println(s.listBookingsForEmployee(1));
////                => ["b4"]
//
//        System.out.println(s.cancelBooking("b2"));
////                => false   // booking never existed
//
//        System.out.println(s.cancelBooking("b1"));
////                => true
//
//        System.out.println(s.cancelBooking("b1"));
////                => false   // already cancelled
//
//        System.out.println(s.getAvailableRooms(10, 20));
////                => [0]     // room0 now free for [10,20

        // --- Edge-case scenario ---
        MeetingRoomScheduler s2 = new MeetingRoomScheduler(1, 1);

        System.out.println(s2.listBookingsForRoom(0));
//                => []

        System.out.println(s2.listBookingsForEmployee(0));
//                => []

        System.out.println(s2.bookRoom("x1", 0, 0, 50, 49));
//                => false   // invalid: startTime > endTime

        System.out.println(s2.getAvailableRooms(70, 10));
//                => []      // invalid query interval

        System.out.println(s2.bookRoom("x2", 0, 0, 60, 60));
//                => true    // valid instant booking at time=60

        System.out.println(s2.getAvailableRooms(60, 60));
//                => []      // room0 is busy at time=60

        System.out.println(s2.bookRoom("x3", 0, 0, 61, 61));
//                => true    // does NOT overlap with [60,60]

        System.out.println(s2.bookRoom("x4", 0, 0, 60, 61));
//                => false   // overlaps with x2 at 60 AND x3 at 61

        System.out.println(s2.cancelBooking("does-not-exist"));
//                => false

        System.out.println(s2.cancelBooking("x2"));
//                => true

        System.out.println(s2.getAvailableRooms(60, 60));
//                => [0]     // now free at time=60
    }
}
