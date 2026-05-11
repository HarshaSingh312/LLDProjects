package org.example;

import java.util.*;

public class Solution1 {
    private Helper helper;

    public Solution1() {}

    public void init(Helper helper) {
        this.helper = helper;
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
        shows.put(showId, new Show(showId, movieId, cinemaId, screenIndex, startTime, endTime));
    }

    public List<String> bookTicket(String ticketId,
                                   int showId, int ticketsCount) {
        if (!shows.containsKey(showId)) return new ArrayList<>();
        Show show = shows.get(showId);
        int cinemaId = show.getCinemaId();
        if (!cinemas.containsKey(cinemaId)) return new ArrayList<>();
        Cinema cinema = cinemas.get(cinemaId);
        List<String> seats = cinema.bookFreeSeats(show.getScreenIndex(), ticketsCount);
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
        int cinemaId = show.getCinemaId();
        if (!cinemas.containsKey(cinemaId)) return false;
        Cinema cinema = cinemas.get(cinemaId);
        List<String> seats = booking.getSeats();
        for (String seat : seats) {
            String[] parts = seat.split("-");
            int index = show.getScreenIndex();
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            cinema.markSeatFree(index, row, col);
        }
        booking.setCancelled(true);
        return true;
    }

    public int getFreeSeatsCount(int showId) {
        if (!shows.containsKey(showId)) return 0;
        Show show = shows.get(showId);
        int cinemaId = show.getCinemaId();
        if (!cinemas.containsKey(cinemaId)) return 0;
        Cinema cinema = cinemas.get(cinemaId);
        return cinema.getFreeSeats(show.getScreenIndex());
    }

    public List<Integer> listCinemas(int movieId, int cityId) {
        List<Integer> filteredCinema = new ArrayList<>();
        for (int cinemaId : cinemas.keySet()) {
            Cinema cinema = cinemas.get(cinemaId);
            if (cinema.getCity() == cityId) {
                filteredCinema.add(cinema.getId());
            }
        }

        List<Integer> result = new ArrayList<>();
        for (int showID : shows.keySet()) {
            Show show = shows.get(showID);
            if (filteredCinema.contains(show.getCinemaId()) && show.getMovieId() == movieId) {
                result.add(show.getCinemaId());
            }
        }
        result.sort(Integer::compareTo);
        return result;
    }

    public List<Integer> listShows(int movieId, int cinemaId) {
        List<Integer> result = new ArrayList<>();
        for (int showID : shows.keySet()) {
            Show show = shows.get(showID);
            if (show.getMovieId() == movieId && show.getCinemaId() == cinemaId) {
                result.add(showID);
            }
        }
        result.sort(Integer::compareTo);
        return result;
    }

    // ---- Inner Classes ----

    static class Show {
        private int showId;
        private int movieId;
        private int cinemaId;
        private int screenIndex;
        private long startTime;
        private long endTime;

        public Show(int showId, int movieId, int cinemaId, int screenIndex, long startTime, long endTime) {
            this.showId = showId;
            this.movieId = movieId;
            this.cinemaId = cinemaId;
            this.screenIndex = screenIndex;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public int getMovieId() { return movieId; }
        public int getCinemaId() { return cinemaId; }
        public int getScreenIndex() { return screenIndex; }
    }

    static class Cinema {
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
            for (int i = 0; i < count; i++)
                for (int row = 0; row < screenRow; row++)
                    for (int col = 0; col < screenColum; col++)
                        seats[i][row][col] = false;
        }

        public int getId() { return id; }
        public int getCity() { return city; }

        public int getFreeSeats(int index) {
            int availableSeats = 0;
            for (int row = 0; row < screenRow; row++)
                for (int col = 0; col < screenColum; col++)
                    if (!seats[index][row][col]) availableSeats++;
            return availableSeats;
        }

        public void markSeatFree(int index, int row, int col) {
            seats[index][row][col] = false;
        }

        public void markSeatOccupied(int index, int row, int col) {
            seats[index][row][col] = true;
        }

        public List<String> bookFreeSeats(int index, int count) {
            List<String> availableSeats = new ArrayList<>();
            for (int row = 0; row < screenRow; row++) {
                int conti = 0;
                for (int j = 0; j < screenColum; j++) {
                    if (seats[index][row][j]) {
                        conti = 0;
                    } else {
                        conti++;
                        if (conti >= count) {
                            for (int k = j - conti + 1; k <= j; k++) {
                                availableSeats.add(row + "-" + k);
                                seats[index][row][k] = true;
                            }
                            return availableSeats;
                        }
                    }
                }
            }
            for (int row = 0; row < screenRow && availableSeats.size() < count; row++) {
                for (int col = 0; col < screenColum && availableSeats.size() < count; col++) {
                    if (!seats[index][row][col]) {
                        availableSeats.add(row + "-" + col);
                        seats[index][row][col] = true;
                    }
                }
            }
            if (availableSeats.size() >= count) return availableSeats;
            for (String s : availableSeats) {
                String[] parts = s.split("-");
                seats[index][Integer.parseInt(parts[0])][Integer.parseInt(parts[1])] = false;
            }
            return new ArrayList<>();
        }
    }

    static class Booking {
        private String ticketId;
        private int showId;
        private int ticketCount;
        private boolean isCancelled;
        private List<String> seats;

        public Booking(String ticketId, int showId, int ticketCount, List<String> seats) {
            this.ticketId = ticketId;
            this.showId = showId;
            this.ticketCount = ticketCount;
            this.isCancelled = false;
            this.seats = seats;
        }

        public int getShowId() { return showId; }
        public boolean isCancelled() { return isCancelled; }
        public void setCancelled(boolean cancelled) { isCancelled = cancelled; }
        public List<String> getSeats() { return seats; }
    }

    static class Helper {
        void print(String s) { System.out.print(s); }
        void println(String s) { print(s + "\n"); }
    }
}
