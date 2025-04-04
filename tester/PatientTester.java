package tester;

import java.util.List;
import java.util.Scanner;
import model.Patient;
import model.PatientDAO;

public class PatientTester {

    private static final Scanner scanner = new Scanner(System.in);
    private static final PatientDAO patientDAO = new PatientDAO();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Patient Management System ===");
            System.out.println("1. Add Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. Search Patient by ID");
            System.out.println("4. Update Patient");
            System.out.println("5. Delete Patient");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addPatient();
                    break;
                case 2:
                    viewAllPatients();
                    break;
                case 3:
                    searchPatient();
                    break;
                case 4:
                    updatePatient();
                    break;
                case 5:
                    deletePatient();
                    break;
                case 6:
                    System.out.println("Exiting... Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addPatient() {
        System.out.println("\nEnter Patient Details:");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Date of Birth (YYYY-MM-DD): ");
        String dateOfBirth = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("PhoneNo: ");
        String phoneNo = scanner.nextLine();

        System.out.print("Street: ");
        String street = scanner.nextLine();

        System.out.print("Town: ");
        String town = scanner.nextLine();

        System.out.print("County: ");
        String county = scanner.nextLine();

        System.out.print("Eircode: ");
        String eircode = scanner.nextLine();

        System.out.print("Do you have a medical card? (Yes/No): ");
        Boolean medCard = getBooleanInput();

        Double amtOwed = 0.00;

        Patient patient = new Patient(0, firstName, lastName, dateOfBirth, phoneNo, email, street, town, county,
                eircode, medCard, amtOwed);
        patientDAO.addPatient(patient);

        System.out.println("Patient added successfully!");
    }

    private static void viewAllPatients() {
        List<Patient> patients = patientDAO.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            System.out.println("\nList of Patients:");
            for (Patient p : patients) {
                System.out.println("ID: " + p.getPatientID() + " | Name: " + p.getFirstName() + " " + p.getLastName()
                        + " | Email: " + p.getEmail());
            }
        }
    }

    private static void searchPatient() {
        System.out.print("\nEnter Patient ID to search: ");
        int id = getIntInput();
        Patient patient = patientDAO.getPatientById(id);

        if (patient != null) {
            System.out.println("\nPatient Found:");
            System.out.println("ID: " + patient.getPatientID());
            System.out.println("Name: " + patient.getFirstName() + " " + patient.getLastName());
            System.out.println("Email: " + patient.getEmail());
            System.out.println("Date of Birth: " + patient.getDOB());
            System.out.println("Street: " + patient.getStreet());
            System.out.println("Town: " + patient.getTown());
            System.out.println("County: " + patient.getCounty());
            System.out.println("Eircode: " + patient.getEircode());
            System.out.println("Medical Card: " + patient.getMedCard());
            System.out.println("Medical Card: " + patient.getAmtOwed());
        } else {
            System.out.println("Patient not found.");
        }
    }

    private static void updatePatient() {
        System.out.print("\nEnter Patient ID to update: ");
        int id = getIntInput();
        Patient existingPatient = patientDAO.getPatientById(id);

        if (existingPatient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println("\nUpdating Patient Details (Leave blank to keep existing values):");

        System.out.print("First Name (" + existingPatient.getFirstName() + "): ");
        String firstName = scanner.nextLine();
        if (firstName.isEmpty()) {
            firstName = existingPatient.getFirstName();
        }

        System.out.print("Last Name (" + existingPatient.getLastName() + "): ");
        String lastName = scanner.nextLine();
        if (lastName.isEmpty()) {
            lastName = existingPatient.getLastName();
        }

        System.out.print("Date of Birth (" + existingPatient.getDOB() + "): ");
        String dateOfBirth = scanner.nextLine();
        if (dateOfBirth.isEmpty()) {
            dateOfBirth = existingPatient.getDOB();
        }

        System.out.print("Phone Number (" + existingPatient.getPhoneNo() + "): ");
        String phoneNo = scanner.nextLine();
        if (phoneNo.isEmpty()) {
            phoneNo = existingPatient.getPhoneNo();
        }

        System.out.print("Email (" + existingPatient.getEmail() + "): ");
        String email = scanner.nextLine();
        if (email.isEmpty()) {
            email = existingPatient.getEmail();
        }

        System.out.print("Street (" + existingPatient.getStreet() + "): ");
        String street = scanner.nextLine();
        if (street.isEmpty()) {
            street = existingPatient.getStreet();
        }

        System.out.print("Town (" + existingPatient.getTown() + "): ");
        String town = scanner.nextLine();
        if (town.isEmpty()) {
            town = existingPatient.getTown();
        }

        System.out.print("County (" + existingPatient.getCounty() + "): ");
        String county = scanner.nextLine();
        if (county.isEmpty()) {
            county = existingPatient.getCounty();
        }

        System.out.print("Eircode (" + existingPatient.getEircode() + "): ");
        String eircode = scanner.nextLine();
        if (eircode.isEmpty()) {
            eircode = existingPatient.getEircode();
        }

        System.out.print("Medical Card (" + existingPatient.getMedCard() + "): ");
        String medCardInput = scanner.nextLine();
        Boolean medCard = medCardInput.isEmpty() ? existingPatient.getMedCard() : Boolean.parseBoolean(medCardInput);

        System.out.print("Amount Owed (" + existingPatient.getAmtOwed() + "): ");
        Double amtOwed = existingPatient.getAmtOwed(); // Default to current amount owed
        if (scanner.hasNextDouble()) {
            amtOwed = scanner.nextDouble();
        }

        Patient updatedPatient = new Patient(id, firstName, lastName, dateOfBirth, phoneNo, email, street, town, county,
                eircode, medCard, amtOwed);
        patientDAO.updatePatient(updatedPatient);

        System.out.println("Patient updated successfully!");
    }

    private static void deletePatient() {
        System.out.print("\nEnter Patient ID to delete: ");
        int id = getIntInput();
        Patient patient = patientDAO.getPatientById(id);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.print("Are you sure you want to delete " + patient.getFirstName() + " " + patient.getLastName()
                + "? (yes/no): ");
        String confirmation = scanner.nextLine().toLowerCase();

        if (confirmation.equals("yes")) {
            patientDAO.deletePatient(id);
            System.out.println("Patient deleted successfully.");
        } else {
            System.out.println("Deletion canceled.");
        }
    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter a number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private static Boolean getBooleanInput() {
        while (true) {
            String input = scanner.nextLine().toLowerCase();
            if (input.equals("yes")) {
                return true;
            } else if (input.equals("no")) {
                return false;
            } else {
                System.out.print("Invalid input. Please enter 'yes' or 'no': ");
            }
        }
    }
}