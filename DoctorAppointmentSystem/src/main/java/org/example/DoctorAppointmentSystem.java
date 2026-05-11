//package org.example;

import java.util.*;
import java.util.stream.Collectors;

class Utils {
    public static int getTimeInMins(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int min = Integer.parseInt(parts[1]);
        return hour * 60 + min;
    }

    public static List<Slot> getSlotsFromString(List<String> slots, String doctorName) {
        return slots.stream().map(slot -> {
            String[] parts = slot.split("-");
            return new Slot(getTimeInMins(parts[0]), getTimeInMins(parts[1]), doctorName);
        }).collect(Collectors.toList());
    }

    public static String getTimingString(int timeInMins) {
        int hours = timeInMins / 60;
        int minutes = timeInMins % 60;
        String hoursS = hours < 10 ? "0" + hours : String.valueOf(hours);
        String minutesS = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
        return hoursS + ":" + minutesS;
    }
}

class Slot {
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

class WaitList {
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

class Appointment {
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

enum AppointmentStatus {
    BOOKED,
    CANCELLED
}

enum DoctorSpecality {
    Cardiologist,
    Dermatologist,
    Orthopedic,
    GeneralPhysician
}


class Doctor {
    private String doctorName;
    private String speciality;
    private List<String> slots;

    public Doctor(String doctorName, String speciality, List<String> slots) {
        this.doctorName = doctorName;
        this.speciality = speciality;
        this.slots = slots;
    }

    public Boolean isSlotPresent(String appointmentTime) {
        for (String slot: slots) {
            String[] slotSplit = slot.split("-");
            if (Utils.getTimeInMins(slotSplit[0]) <= Utils.getTimeInMins(appointmentTime) &&
                    Utils.getTimeInMins(appointmentTime) <= Utils.getTimeInMins(slotSplit[1])) {
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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public List<String> getSlots() {
        return slots;
    }

    public void setSlots(List<String> slots) {
        this.slots = slots;
    }
}

public class DoctorAppointmentSystem {

    HashMap<String, Doctor> doctorsDB = new HashMap<>();
    HashMap<String, Appointment> appointmentDB = new HashMap<>();
    HashMap<String, List<WaitList>> waitingListDB = new HashMap<>();
    private final List<String> specalities = List.of("Cardiologist", "Dermatologist", "Orthopedic", "General Physician");

    public DoctorAppointmentSystem() {

    }

    private boolean isSlotValid(List<String> slots) {
        for (String slot: slots) {
            String[] splitSlots = slot.split("-");
            int startSlot = Utils.getTimeInMins(splitSlots[0]);
            int endSlot = Utils.getTimeInMins(splitSlots[1]);
            int startingStartSlot = Utils.getTimeInMins("09:00");
            int endingStartSlot = Utils.getTimeInMins("20:00");
            int startingEndSlot = Utils.getTimeInMins("10:00");
            int endingEndSlot = Utils.getTimeInMins("21:00");
            if (startSlot < startingStartSlot || startSlot > endingStartSlot) return false;
            if (endSlot < startingEndSlot || endSlot > endingEndSlot) return false;
        }
        return true;
    }

    private boolean isSpecialityValid(String speciality) {
        return specalities.contains(speciality);
    }

    public void registerDoctor(String doctorName, String speciality, List<String> slots) {
        if (doctorName.isBlank()) return;
        if (!isSlotValid(slots)) return;
        if (!isSpecialityValid(speciality)) return;
//        System.out.println("doctorName " + doctorName);
        doctorsDB.put(doctorName, new Doctor(doctorName, speciality, slots));
    }

    public void addToWaitingList(String appointmentId, String patientId, String doctorName, String bookingId) {
        List<WaitList> list = waitingListDB.getOrDefault(appointmentId, new ArrayList<>());
        list.add(new WaitList(patientId, doctorName, bookingId));
        waitingListDB.put(appointmentId, list);
    }

    public WaitList getFirstWaitingUserAndRemove(String appointmentId) {
        List<WaitList> user = waitingListDB.getOrDefault(appointmentId, null);
        if (Objects.isNull(user) || user.isEmpty()) return null;
        return user.remove(0);
    }

    public List<WaitList> getWaitingUsers(String appointmentId) {
        return waitingListDB.getOrDefault(appointmentId, new ArrayList<>());
    }

    private Appointment getAppointmentsForDoctorBySlot(String doctorName, int startTime, int endTime) {
        for (Appointment appointment: appointmentDB.values()) {
            if (appointment.getDoctorName().equals(doctorName) && startTime <= Utils.getTimeInMins(appointment.getStartTime())
                    && endTime >= Utils.getTimeInMins(appointment.getStartTime()) && appointment.getStatus().equals(AppointmentStatus.BOOKED)) {
                return appointment;
            }
        }
        return null;
    }

    public String bookAppointment(String bookingId, String patientId, String doctorName, String startTime, boolean addToWaitlistIfBooked) {
        if (patientId.isBlank()) return "";
        if (doctorName.isBlank()) return "";
        Doctor doctor = doctorsDB.get(doctorName);
        if (Objects.isNull(doctor)) return "";
//        if (!doctor.isSlotPresent(startTime)) return "";
        // Check if doc has any other appointment at same time
        for (Appointment appointment: appointmentDB.values()) {
            if (appointment.getDoctorName().equals(doctorName) && appointment.getStartTime().equals(startTime)) {
                // doctor is already taken
                if (addToWaitlistIfBooked) {
                    addToWaitingList(appointment.getBookingId(), patientId, doctorName, bookingId);
                    return "Added to the waitlist";
                } else {
                    return "Slot already booked";
                }
            }
        }

        // Check if doc has any other appointment at same time
        for (Appointment appointment: appointmentDB.values()) {
            if (appointment.getPatientId().equals(patientId) && appointment.getStartTime().equals(startTime)) {
                return "Slot already booked";
            }
        }

        // Confirm booking
        appointmentDB.put(bookingId, new Appointment(bookingId, patientId, doctorName, startTime, addToWaitlistIfBooked));
        return "BOOKED";
    }

    public List<String> showAvailabilityBySpeciality(String speciality) {
        List<Slot> result = new ArrayList<>();
        for (Doctor doctor: doctorsDB.values()) {
            if (doctor.getSpeciality().equals(speciality)) {
                List<Slot> doctorAvailableSlot = Utils.getSlotsFromString(doctor.getSlots(), doctor.getDoctorName());
                for (Slot slot: doctorAvailableSlot) {
                    Appointment a = getAppointmentsForDoctorBySlot(doctor.getDoctorName(), slot.getStart(), slot.getEnd());
//                    System.out.println("a " + a);
//                    if (!Objects.isNull(a) && slot.getStart() != Utils.getTimeInMins(a.getStartTime())) {
//                        result.add(new Slot(slot.getStart(), Utils.getTimeInMins(a.getStartTime()), doctor.getDoctorName()));
//                    }
                    if (Objects.isNull(a)) {
                        result.add(new Slot(slot.getStart(), slot.getEnd(), doctor.getDoctorName()));
                    }
                }
            }
        }
        result.sort(Comparator.comparing(Slot::getStart).thenComparing(Slot::getDoctorName));
        List<String> ans = new ArrayList<>();
        for (Slot slot : result) {
            ans.add(String.format("Dr.%s: (%s-%s)", slot.getDoctorName(), Utils.getTimingString(slot.getStart()), Utils.getTimingString(slot.getEnd())));
        }
        return ans;
    }

    public List<String> cancelBooking(String bookingId) {
        List<String> result = new ArrayList<>();
        Appointment appointment = appointmentDB.get(bookingId);

        for (String appointmentId: waitingListDB.keySet()) {
            List<WaitList> waitLists = waitingListDB.get(appointmentId);
            for (WaitList waitList: waitLists) {
                if (waitList.getAppointmentId().equals(bookingId)) {
                    waitLists.remove(waitList);
                    waitingListDB.put(appointmentId, waitLists);
                    result.add("Booking Cancelled");
                    return result;
                }
            }
        }

        if (Objects.isNull(appointment)) {
            result.add("Invalid booking id");
            return result;
        }
        WaitList waitingList = getFirstWaitingUserAndRemove(bookingId);
        // Confirmed waiting list
        boolean isWaitingListAdded = false;
        if (!Objects.isNull(waitingList)) {
            appointmentDB.put(waitingList.getAppointmentId(), new Appointment(waitingList.getAppointmentId(),
                    waitingList.getPatientName(), waitingList.getDoctorName(), appointment.getStartTime(),false));
            waitingListDB.put(waitingList.getAppointmentId(), getWaitingUsers(bookingId));
            isWaitingListAdded = true;
        }
        // Remove current appointment;
        appointmentDB.remove(bookingId);
        result.add("Booking Cancelled");
        if (isWaitingListAdded) {
            result.add("Booking confirmed for Booking id: " + waitingList.getAppointmentId());
        }
        appointmentDB.remove(bookingId);
        return result;
    }

    public List<String> showAppointmentsBooked(String userName) {
        List<String> result = new ArrayList<>();
        // Adding doctors
        for (Appointment appointment: appointmentDB.values()) {
            if (appointment.getPatientId().equals(userName)) {
                result.add(String.format("Booking id: %s, Dr %s %s", appointment.getBookingId(), appointment.getDoctorName(), appointment.getStartTime()));
            }
        }

        // Adding Patient
        for (Appointment appointment: appointmentDB.values()) {
            if (appointment.getDoctorName().equals(userName)) {
                result.add(String.format("Booking id: %s, %s %s", appointment.getBookingId(), appointment.getPatientId(), appointment.getStartTime()));
            }
        }
        return result;
    }
}
