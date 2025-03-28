package tester;

import model.AppointmentCRUD;
import model.AppointmentCRUD.Appointment;
import java.sql.Date;
import java.sql.Time;

public class AppointmentTester {
    public static void main(String[] args) {
        // Create an instance of AppointmentCRUD
        AppointmentCRUD appointmentCRUD = new AppointmentCRUD();

        // Test CREATE: Add a new appointment
        Appointment newAppointment = new Appointment(
                Date.valueOf("2025-03-10"), // Date of appointment
                Time.valueOf("14:30:00"), // Time of appointment
                false, // Attended status
                1, // TreatmentID (example value)
                10, // PatientID (example value)
                2 // DentistID (example value)
        );
        
        appointmentCRUD.addAppointment(newAppointment);

        // Test READ: Get all appointments
        System.out.println("\nAll Appointments:");
        appointmentCRUD.getAllAppointments().forEach(appointment -> {
            System.out.println("Appointment ID: " + appointment.getAppointmentID());
            System.out.println("Date: " + appointment.getDateOfAppointment());
            System.out.println("Time: " + appointment.getTimeOfAppointment());
            System.out.println("Attended: " + appointment.isAttended());
            System.out.println("Treatment ID: " + appointment.getTreatmentID());
            System.out.println("Patient ID: " + appointment.getPatientID());
            System.out.println("Dentist ID: " + appointment.getDentistID());
            System.out.println("-----------------------------");
        });

        // Test UPDATE: Modify an existing appointment
        if (!appointmentCRUD.getAllAppointments().isEmpty()) {
            Appointment firstAppointment = appointmentCRUD.getAllAppointments().get(0);
            firstAppointment.setDateOfAppointment(Date.valueOf("2025-03-12")); // New date
            firstAppointment.setTimeOfAppointment(Time.valueOf("16:00:00")); // New time
            firstAppointment.setAttended(true); // Update attended status

            appointmentCRUD.updateAppointment(firstAppointment);
            System.out.println("\nUpdated Appointment:");
            System.out.println("Appointment ID: " + firstAppointment.getAppointmentID());
            System.out.println("Date: " + firstAppointment.getDateOfAppointment());
            System.out.println("Time: " + firstAppointment.getTimeOfAppointment());
            System.out.println("Attended: " + firstAppointment.isAttended());
        }

        // Test DELETE: Remove an appointment
        if (!appointmentCRUD.getAllAppointments().isEmpty()) {
            int appointmentIDToDelete = appointmentCRUD.getAllAppointments().get(0).getAppointmentID();
            appointmentCRUD.deleteAppointment(appointmentIDToDelete);
            System.out.println("\nDeleted Appointment with ID: " + appointmentIDToDelete);
        }
    }
}
