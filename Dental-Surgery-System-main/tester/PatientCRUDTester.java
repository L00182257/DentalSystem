package tester;

import model.PatientCRUD;
import model.Patient;
import java.util.List;

public class PatientCRUDTester {

    public static void main(String[] args) {
        // Create an instance of PatientCRUD
        PatientCRUD patientCRUD = new PatientCRUD();

        // 1. Test CREATE: Add a new patient
        Patient newPatient = new Patient(1,
                "John", // First name
                "Doe",  // Last name
                "1990-01-01", // Date of birth (YYYY-MM-DD)
                "johndoe@example.com", // Email
                "123 Elm Street", // Street
                "Springfield", // Town
                "SomeCounty", // County
                "12345", // Eircode
                true // Medical Card (example: true)
        );
        
        patientCRUD.addPatient(newPatient); // Adding new patient to the database

        // 2. Test READ: Get all patients and print them
        System.out.println("\nAll Patients:");
        List<Patient> patients = patientCRUD.getAllPatients();
        patients.forEach(patient -> {
            System.out.println("Patient ID: " + patient.getId());
            System.out.println("First Name: " + patient.getFirstName());
            System.out.println("Last Name: " + patient.getLastName());
            System.out.println("DOB: " + patient.getDOB());
            System.out.println("Email: " + patient.getEmail());
            System.out.println("Street: " + patient.getStreet());
            System.out.println("Town: " + patient.getTown());
            System.out.println("County: " + patient.getCounty());
            System.out.println("Eircode: " + patient.getEircode());
            System.out.println("Medical Card: " + patient.getMedCard());
            System.out.println("-------------------------------");
        });

        // 3. Test SEARCH: Get patient by ID
        System.out.println("\nSearching for Patient with ID 1:");
        Patient patientById = patientCRUD.getPatientById(1);
        if (patientById != null) {
            System.out.println("Patient ID: " + patientById.getId());
            System.out.println("First Name: " + patientById.getFirstName());
            System.out.println("Last Name: " + patientById.getLastName());
            System.out.println("DOB: " + patientById.getDOB());
            System.out.println("Email: " + patientById.getEmail());
            System.out.println("Street: " + patientById.getStreet());
            System.out.println("Town: " + patientById.getTown());
            System.out.println("County: " + patientById.getCounty());
            System.out.println("Eircode: " + patientById.getEircode());
            System.out.println("Medical Card: " + patientById.getMedCard());
        } else {
            System.out.println("Patient not found.");
        }

        // 4. Test UPDATE: Modify a patient (for example, changing their email)
        if (!patients.isEmpty()) {
            Patient patientToUpdate = patients.get(0);
            patientToUpdate.setEmail("newemail@example.com"); // Update email
            patientCRUD.updatePatient(patientToUpdate);
            System.out.println("\nUpdated Patient Details:");
            System.out.println("Patient ID: " + patientToUpdate.getId());
            System.out.println("First Name: " + patientToUpdate.getFirstName());
            System.out.println("Last Name: " + patientToUpdate.getLastName());
            System.out.println("DOB: " + patientToUpdate.getDOB());
            System.out.println("Email: " + patientToUpdate.getEmail());
            System.out.println("Street: " + patientToUpdate.getStreet());
            System.out.println("Town: " + patientToUpdate.getTown());
            System.out.println("County: " + patientToUpdate.getCounty());
            System.out.println("Eircode: " + patientToUpdate.getEircode());
            System.out.println("Medical Card: " + patientToUpdate.getMedCard());
        }

        // // 5. Test DELETE: Remove a patient
        // if (!patients.isEmpty()) {
        //     int patientIDToDelete = patients.get(0).getId();
        //     System.out.println("\nDeleting Patient with ID: " + patientIDToDelete);
        //     patientCRUD.deletePatient(patientIDToDelete);
        //     System.out.println("Patient with ID " + patientIDToDelete + " has been deleted.");
        // }
    }
}
