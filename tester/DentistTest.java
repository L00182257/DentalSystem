package tester;

import java.util.List;
import java.util.Scanner;
import model.Dentist;
import model.DentistDAO;

public class DentistTest {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DentistDAO dentistDAO = new DentistDAO();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Dentist Management System ===");
            System.out.println("1. Add Dentist");
            System.out.println("2. View All Dentists");
            System.out.println("3. Search Dentist by ID");
            System.out.println("4. Update Dentist");
            System.out.println("5. Delete Dentist");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addDentist();
                    break;
                case 2:
                    viewAllDentists();
                    break;
                case 3:
                    searchDentist();
                    break;
                case 4:
                    updateDentist();
                    break;
                case 5:
                    deleteDentist();
                    break;
                case 6:
                    System.out.println("Exiting... Goodbye!");
                    scanner.close();  // Close Scanner before exiting
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Add a new dentist
    private static void addDentist() {
        System.out.println("\nEnter Dentist Details:");
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Awarding Body: ");
        String awardingBody = scanner.nextLine();

        System.out.print("Specialty: ");
        String specialty = scanner.nextLine();

        System.out.print("Date of Birth (YYYY-MM-DD): ");
        String dateOfBirth = scanner.nextLine();

        System.out.print("Phone Number: ");
        int phoneNo = getIntInput();

        Dentist dentist = new Dentist(0, lastName, firstName, awardingBody, specialty, dateOfBirth, phoneNo);
        dentistDAO.addDentist(dentist);

        System.out.println("Dentist added successfully!");
    }

    // View all dentists
    private static void viewAllDentists() {
        List<Dentist> dentists = dentistDAO.getAllDentists();
        if (dentists.isEmpty()) {
            System.out.println("No dentists found.");
        } else {
            System.out.println("\nList of Dentists:");
            for (Dentist d : dentists) {
                System.out.println(d.getDentistID() + ": " + d.getFirstName() + " " + d.getLastName() + " | " + d.getSpecialty());
            }
        }
    }

    // Search dentist by ID
    private static void searchDentist() {
        System.out.print("\nEnter Dentist ID to search: ");
        int id = getIntInput();
        Dentist dentist = dentistDAO.getDentistById(id);

        if (dentist != null) {
            System.out.println("\nDentist Found:");
            System.out.println("ID: " + dentist.getDentistID());
            System.out.println("Name: " + dentist.getFirstName() + " " + dentist.getLastName());
            System.out.println("Awarding Body: " + dentist.getAwardingBody());
            System.out.println("Specialty: " + dentist.getSpecialty());
            System.out.println("Date of Birth: " + dentist.getDateOfBirth());
            System.out.println("Phone No: " + dentist.getPhoneNo());
        } else {
            System.out.println("Dentist not found.");
        }
    }

    // Update dentist details
    private static void updateDentist() {
        System.out.print("\nEnter Dentist ID to update: ");
        int id = getIntInput();
        Dentist existingDentist = dentistDAO.getDentistById(id);

        if (existingDentist == null) {
            System.out.println("Dentist not found.");
            return;
        }

        System.out.println("\nUpdating Dentist Details (Leave blank to keep existing values):");

        System.out.print("Last Name (" + existingDentist.getLastName() + "): ");
        String lastName = scanner.nextLine();
        if (lastName.isEmpty()) lastName = existingDentist.getLastName();

        System.out.print("First Name (" + existingDentist.getFirstName() + "): ");
        String firstName = scanner.nextLine();
        if (firstName.isEmpty()) firstName = existingDentist.getFirstName();

        System.out.print("Awarding Body (" + existingDentist.getAwardingBody() + "): ");
        String awardingBody = scanner.nextLine();
        if (awardingBody.isEmpty()) awardingBody = existingDentist.getAwardingBody();

        System.out.print("Specialty (" + existingDentist.getSpecialty() + "): ");
        String specialty = scanner.nextLine();
        if (specialty.isEmpty()) specialty = existingDentist.getSpecialty();

        System.out.print("Date of Birth (" + existingDentist.getDateOfBirth() + "): ");
        String dateOfBirth = scanner.nextLine();
        if (dateOfBirth.isEmpty()) dateOfBirth = existingDentist.getDateOfBirth();

        System.out.print("Phone Number (" + existingDentist.getPhoneNo() + "): ");
        String phoneNoInput = scanner.nextLine();
        int phoneNo = phoneNoInput.isEmpty() ? existingDentist.getPhoneNo() : Integer.parseInt(phoneNoInput);

        Dentist updatedDentist = new Dentist(id, lastName, firstName, awardingBody, specialty, dateOfBirth, phoneNo);
        dentistDAO.updateDentist(updatedDentist);

        System.out.println("Dentist updated successfully!");
    }

    // Delete a dentist by ID
    private static void deleteDentist() {
        System.out.print("\nEnter Dentist ID to delete: ");
        int id = getIntInput();
        Dentist dentist = dentistDAO.getDentistById(id);

        if (dentist == null) {
            System.out.println("Dentist not found.");
            return;
        }

        System.out.print("Are you sure you want to delete " + dentist.getFirstName() + " " + dentist.getLastName() + "? (yes/no): ");
        String confirmation = scanner.nextLine().toLowerCase();

        if (confirmation.equals("yes")) {
            dentistDAO.deleteDentist(id);
            System.out.println("Dentist deleted successfully.");
        } else {
            System.out.println("Deletion canceled.");
        }
    }

    // Get valid integer input
    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter a number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();  // Clear buffer
        return value;
    }
}
