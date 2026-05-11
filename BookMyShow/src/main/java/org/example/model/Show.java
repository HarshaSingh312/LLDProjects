package org.example.model;

public class Show {

    private int showId;

    public int getMovieId() {
        return movieId;
    }



    private int movieId;

    public int getCinemaId() {
        return cinemaId;
    }

    private int cinemaId;

    public int getScreenIndex() {
        return screenIndex;
    }

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
}
