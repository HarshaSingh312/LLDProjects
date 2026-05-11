package org.example.model;

public class WaitList {
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    private String patientName;
    private String doctorName;
    private String appointmentId;

    public WaitList(String patientName, String doctorName, String appointmentId) {
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentId = appointmentId;
    }
}
