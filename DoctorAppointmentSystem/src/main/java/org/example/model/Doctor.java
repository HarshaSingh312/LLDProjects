package org.example.model;

import java.util.List;

import static org.example.Utils.getTimeInMins;

public class Doctor {
    private String doctorName;
    private DoctorSpecality speciality;
    private List<String> slots;

    public Doctor(String doctorName, DoctorSpecality speciality, List<String> slots) {
        this.doctorName = doctorName;
        this.speciality = speciality;
        this.slots = slots;
    }

    public Boolean isSlotPresent(String appointmentTime) {
        for (String slot: slots) {
            String[] slotSplit = slot.split("-");
            if (getTimeInMins(slotSplit[0]) <= getTimeInMins(appointmentTime) &&
                    getTimeInMins(appointmentTime) <= getTimeInMins(slotSplit[0])) {
                return false;
            }
        }
        return true;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public DoctorSpecality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(DoctorSpecality speciality) {
        this.speciality = speciality;
    }

    public List<String> getSlots() {
        return slots;
    }

    public void setSlots(List<String> slots) {
        this.slots = slots;
    }
}
