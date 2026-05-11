//package org.example;

/* ****** Copy this default code to your local code editor
and after completing solution, paste it back here for testing ******** */
//import org.example.model.Booking;
//import org.example.model.Cinema;
//import org.example.model.Show;

import java.util.*;

class Booking {
    private String ticketId;

    public int getShowId() {
        return showId;
    }

    private int showId;
    private int ticketCount;

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    private boolean isCancelled;

    public List<String> getSeats() {
        return seats;
    }

    private List<String> seats;

    public Booking(String ticketId, int showId, int ticketCount, List<String> seats) {
        this.ticketId = ticketId;
        this.showId = showId;
        this.ticketCount = ticketCount;
        this.isCancelled = false;
        this.seats = seats;
    }
}


class Show {
    private int showId;
    private int movieId;
    private int cinemaId;
    private int screenIndex;
    private long startTime;
    private long endTime;
    private int screenRow;
    private int screenColum;
    private Boolean[][] seats;

    public Show(int showId, int movieId, int cinemaId, int screenIndex, long startTime, long endTime, int screenRow, int screenColum) {
        this.showId = showId;
        this.movieId = movieId;
        this.cinemaId = cinemaId;
        this.screenIndex = screenIndex;
        this.startTime = startTime;
        this.endTime = endTime;
        this.screenRow = screenRow;
        this.screenColum = screenColum;
        seats = new Boolean[screenRow][screenColum];
        for (int r = 0; r < screenRow; r++)
            for (int c = 0; c < screenColum; c++)
                seats[r][c] = false;
    }

    public int getShowId() { return showId; }
    public int getMovieId() { return movieId; }
    public int getCinemaId() { return cinemaId; }
    public int getScreenIndex() { return screenIndex; }
    public long getStartTime() { return startTime; }

    public int getFreeSeats() {
        int count = 0;
        for (int r = 0; r < screenRow; r++)
            for (int c = 0; c < screenColum; c++)
                if (!seats[r][c]) count++;
        return count;
    }

    public void markSeatFree(int row, int col) { seats[row][col] = false; }

    public List<String> bookFreeSeats(int count) {
        List<String> booked = new ArrayList<>();
        // contiguous seat selection
        for (int row = 0; row < screenRow; row++) {
            int conti = 0;
            for (int j = 0; j < screenColum; j++) {
                if (seats[row][j]) {
                    conti = 0;
                } else {
                    conti++;
                    if (conti >= count) {
                        for (int k = j - conti + 1; k <= j; k++) {
                            booked.add(row + "-" + k);
                            seats[row][k] = true;
                        }
                        return booked;
                    }
                }
            }
        }
        // fallback: assign as we get
        for (int row = 0; row < screenRow && booked.size() < count; row++) {
            for (int col = 0; col < screenColum && booked.size() < count; col++) {
                if (!seats[row][col]) {
                    booked.add(row + "-" + col);
                    seats[row][col] = true;
                }
            }
        }
        if (booked.size() >= count) return booked;
        // rollback
        for (String s : booked) {
            String[] parts = s.split("-");
            seats[Integer.parseInt(parts[0])][Integer.parseInt(parts[1])] = false;
        }
        return new ArrayList<>();
    }
}


class Cinema {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getScreenRow() {
        return screenRow;
    }

    public void setScreenRow(int screenRow) {
        this.screenRow = screenRow;
    }

    public int getScreenColum() {
        return screenColum;
    }

    public void setScreenColum(int screenColum) {
        this.screenColum = screenColum;
    }

    private int id;
    private int city;
    private int count;
    private int screenRow;
    private int screenColum;
    private Boolean[][][] seats;


    public Cinema(int id, int city, int count, int screenRow, int screenColum) {
        this.id = id;
        this.city = city;
        this.count = count;
        this.screenRow = screenRow;
        this.screenColum = screenColum;
        seats = new Boolean[count][screenRow][screenColum];
        for (int i=0; i< count; i++) {
            for (int row = 0; row < screenRow; row++) {
                for (int col = 0; col < screenColum; col++) {
                    seats[i][row][col] = false;
                }
            }
        }
    }

    public int getFreeSeats(int index) {
        int availableSeats = 0;
//        for (int i=0; i< count; i++) {
        for (int row = 0; row < screenRow; row++) {
            for (int col = 0; col < screenColum; col++) {
                if (!seats[index][row][col]) availableSeats++;
            }
        }
//        }
        return availableSeats;
    }

    public void markSeatFree(int index, int row, int col) {
        seats[index][row][col] = false;
    }

    public void markSeatOccupied(int index, int row, int col) {
        seats[index][row][col] = true;
    }

    public List<String> bookFreeSeats(int index, int  count) {
        List<String> availableSeats = new ArrayList<>();
        // continuos seat selection
        for (int row = 0; row < screenRow; row++) {
            int conti = 0;
            for (int j = 0; j <screenColum; j++) {
                if (seats[index][row][j]) {
                    conti = 0;
                } else {
                    conti = conti + 1;
                    if (conti >= count) {
                        String seatS = "";
                        for (int k = j - conti + 1; k <= j; k ++) {
                            seatS = row + "-" + k;
                            availableSeats.add(seatS);
                            seats[index][row][k] = true;
                        }
                        return availableSeats;
                    }
                }
            }
        }

        // Assigning as we get
        for (int row = 0; row < screenRow && availableSeats.size() < count; row++) {
            for (int col = 0; col < screenColum && availableSeats.size() < count; col++) {
                if (!seats[index][row][col]) {
                    availableSeats.add(row + "-" + col);
                    seats[index][row][col] = true;
                }
            }
        }
        if (availableSeats.size() >= count) return availableSeats;
        // not enough seats — rollback
        for (String s : availableSeats) {
            String[] parts = s.split("-");
            seats[index][Integer.parseInt(parts[0])][Integer.parseInt(parts[1])] = false;
        }
        return new ArrayList<>();
    }
}


public class Solution implements Q10MovieBookingInterface{
    private Helper10 helper;
    public Solution(){}
    public void init(Helper10 helper) {
        this.helper=helper;
    }

    HashMap<Integer, Cinema> cinemas = new HashMap<>();
    HashMap<Integer, Show> shows = new HashMap<>();
    HashMap<String, Booking> bookings = new HashMap<>();

    public void addCinema(int cinemaId, int cityId,
                          int screenCount, int screenRow, int screenColumn) {
        if (cinemas.containsKey(cinemaId)) return;
        cinemas.put(cinemaId, new Cinema(cinemaId, cityId, screenCount, screenRow, screenColumn));
    }

    public void addShow(int showId, int movieId, int cinemaId,
                        int screenIndex, long startTime, long endTime) {
        if (shows.containsKey(showId)) return;
        if (!cinemas.containsKey(cinemaId)) return;
        Cinema cinema = cinemas.get(cinemaId);
        shows.put(showId, new Show(showId, movieId, cinemaId, screenIndex, startTime, endTime, cinema.getScreenRow(), cinema.getScreenColum()));
    }

    public List<String> bookTicket(String ticketId,
                                   int showId, int ticketsCount) {
        if (!shows.containsKey(showId)) return new ArrayList<>();
        Show show = shows.get(showId);
        List<String> seats = show.bookFreeSeats(ticketsCount);
        if (seats.size() >= ticketsCount) {
            bookings.put(ticketId, new Booking(ticketId, showId, ticketsCount, seats));
        }
        return seats;
    }

    public boolean cancelTicket(String ticketId) {
        if (!bookings.containsKey(ticketId)) return false;
        Booking booking = bookings.get(ticketId);
        if (booking.isCancelled()) return false;
        Show show = shows.get(booking.getShowId());
        List<String> seats = booking.getSeats();
        for (String seat: seats) {
            String[] parts = seat.split("-");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            show.markSeatFree(row, col);
        }
        booking.setCancelled(true);
        return true;
    }

    public int getFreeSeatsCount(int showId) {
        if (!shows.containsKey(showId)) return 0;
        return shows.get(showId).getFreeSeats();
    }

    public List<Integer> listCinemas(int movieId, int cityId) {
        List<Integer> filteredCinema = new ArrayList<>();
        for (int cinemaId: cinemas.keySet()) {
            System.out.println("cinemaId "+ cinemaId);
            Cinema cinema = cinemas.get(cinemaId);
            if (cinema.getCity() == cityId) {
                filteredCinema.add(cinema.getId());
            }
        }

        List<Integer> result = new ArrayList<>();
        for (int showID: shows.keySet()) {
            System.out.println("showId "+ showID);
            Show show = shows.get(showID);
            if (filteredCinema.contains(show.getCinemaId()) && show.getMovieId() == movieId && !result.contains(show.getCinemaId())) {
                result.add(show.getCinemaId());
            }
        }
        result.sort(Integer::compareTo);
       return result;
    }

    public List<Integer> listShows(int movieId, int cinemaId) {
        List<Show> matched = new ArrayList<>();
        for (int showID: shows.keySet()) {
            Show show = shows.get(showID);
            if (show.getMovieId() == movieId && show.getCinemaId() == cinemaId) {
                matched.add(show);
            }
        }
        matched.sort((a, b) -> {
            if (a.getStartTime() != b.getStartTime()) return Long.compare(b.getStartTime(), a.getStartTime());
            return Integer.compare(a.getShowId(), b.getShowId());
        });
        List<Integer> result = new ArrayList<>();
        for (Show s : matched) result.add(s.getShowId());
        return result;
    }

}

// uncomment below code when you are using your local code editor and
// comment it back again back when you are pasting completed solution in the online CodeZym editor
// this will help avoid unwanted compilation errors and get method autocomplete in your local code editor.
/**
 interface Q10MovieBookingInterface{
 void init(Helper10 helper);
 void addCinema(int cinemaId, int cityId,
 int screenCount, int screenRow, int screenColumn);
 void addShow(int showId, int movieId, int cinemaId,
 int screenIndex, long startTime, long endTime);
 List<String> bookTicket(String ticketId,
 int showId, int ticketsCount);
 boolean cancelTicket(String ticketId);
 int getFreeSeatsCount(int showId);
 // returns cinemaId's of all cinemas which are running a show for given movie
 // cinemaId's are ordered in ascending order
 List<Integer> listCinemas(int movieId, int cityId);
 // returns all showId's of all shows displaying the movie in given cinema
 // showId's are ordered in ascending order
 List<Integer> listShows(int movieId, int cinemaId);

 }

 class Helper10{
 void print(String s){System.out.print(s);}
 void println(String s){print(s+"\n");}
 }
 */
