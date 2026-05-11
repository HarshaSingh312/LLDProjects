package org.example.model;

import java.util.List;

public class Booking {
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
