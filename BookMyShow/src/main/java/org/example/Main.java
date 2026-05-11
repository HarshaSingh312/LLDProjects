package org.example;

public class Main {
    public static void main(String[] args) {
        Solution obj = new Solution();
        obj.init(new Helper10());
        obj.addCinema(0, 1, 4, 5, 10);
        obj.addShow(1, 4, 0, 1, 1710516108725L, 1710523308725L);
        obj.addShow(2, 11, 0, 3, 1710516108725L, 1710523308725L);

        System.out.println(obj.listCinemas(0, 1));       // []\n
        System.out.println(obj.listShows(4, 0));          // [1]
        System.out.println(obj.listShows(11, 0));         // [2]

        System.out.println(obj.getFreeSeatsCount(1));     // 50
        System.out.println(obj.bookTicket("tkt-1", 1, 4)); // [0-0, 0-1, 0-2, 0-3]
        System.out.println(obj.getFreeSeatsCount(1));     // 46
    }
}
