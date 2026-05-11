package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Cinema {
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

        // Assiging as we get
        for (int row = 0; row < screenRow; row++) {
            for (int col = 0; col < screenColum; col++) {
                if (!seats[index][row][col]) {
                    String seatS = row + "-" + col;
                    availableSeats.add(seatS);
                    seats[index][row][col] = true;
                    if (availableSeats.size() >= count) return availableSeats;
                }
            }
        }
        if (availableSeats.size() >= count) return availableSeats;
        return new ArrayList<>();
    }
}
