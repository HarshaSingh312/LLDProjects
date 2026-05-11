package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DoctorAppointmentSystem system = new DoctorAppointmentSystem();

        // Example 1: Register doctors and view availability by speciality
        system.registerDoctor("Curious", "Cardiologist", List.of("12:00-13:00", "16:00-17:00"));
        system.registerDoctor("Alpha", "Cardiologist", List.of("09:00-10:00"));
        System.out.println(system.showAvailabilityBySpeciality("Cardiologist"));

        // Example 2: Book an appointment and verify availability + booked list
        System.out.println(system.bookAppointment("b1", "PatientA", "Alpha", "09:00", false));
        System.out.println(system.showAvailabilityBySpeciality("Cardiologist"));
        System.out.println(system.showAppointmentsBooked("PatientA"));
        System.out.println(system.showAppointmentsBooked("Alpha"));

        // Example 3: Waitlist promotion after cancellation
        System.out.println(system.bookAppointment("b2", "PatientB", "Curious", "12:00", false));
        System.out.println(system.bookAppointment("b3", "PatientC", "Curious", "12:00", true));
        System.out.println(system.cancelBooking("b2"));
        System.out.println(system.showAppointmentsBooked("PatientC"));
        System.out.println(system.showAppointmentsBooked("Curious"));
    }
}
