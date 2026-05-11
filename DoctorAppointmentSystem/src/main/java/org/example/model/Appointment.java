package org.example.model;

public class Appointment {
    String bookingId;
    String patientId;
    String doctorName;
    String startTime;
    boolean addToWaitlistIfBooked;
    AppointmentStatus status;

    public Appointment(String bookingId, String patientId, String doctorName, String startTime, boolean addToWaitlistIfBooked) {
        this.bookingId = bookingId;
        this.patientId = patientId;
        this.doctorName = doctorName;
        this.startTime = startTime;
        this.addToWaitlistIfBooked = addToWaitlistIfBooked;
        this.status = AppointmentStatus.BOOKED;
    }


    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public boolean isAddToWaitlistIfBooked() {
        return addToWaitlistIfBooked;
    }

    public void setAddToWaitlistIfBooked(boolean addToWaitlistIfBooked) {
        this.addToWaitlistIfBooked = addToWaitlistIfBooked;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}
