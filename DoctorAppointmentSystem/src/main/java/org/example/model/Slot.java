package org.example.model;

public class Slot {
    int start;
    int end;
    String doctorName;

    public Slot(int start, int end, String doctorName) {
        this.start = start;
        this.end = end;
        this.doctorName = doctorName;
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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
