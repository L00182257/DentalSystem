package tester;

import java.util.List;
import java.util.Scanner;
import model.TreatmentCRUD;
import model.TreatmentCRUD.Treatment;

public class TreatmentCRUDTester {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TreatmentCRUD treatmentCRUD = new TreatmentCRUD();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Treatment Management System ===");
            System.out.println("1. Add Treatment");
            System.out.println("2. View All Treatments");
            System.out.println("3. Search Treatment by ID");
            System.out.println("4. Update Treatment");
            System.out.println("5. Delete Treatment");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addTreatment();
                    break;
                case 2:
                    viewAllTreatments();
                    break;
                case 3:
                    searchTreatment();
                    break;
                case 4:
                    updateTreatment();
                    break;
                case 5:
                    deleteTreatment();
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

    private static void addTreatment() {
        System.out.println("\nEnter Treatment Details:");
        System.out.print("Treatment Type: ");
        String treatmentType = scanner.nextLine();
        System.out.print("Price: ");
        int price = getIntInput();
        System.out.print("Length (minutes): ");
        int length = getIntInput();

        Treatment treatment = new Treatment(0, treatmentType, price, length);
        treatmentCRUD.addTreatment(treatment);
        System.out.println("Treatment added successfully!");
    }

    private static void viewAllTreatments() {
        List<Treatment> treatments = treatmentCRUD.getAllTreatments();
        if (treatments.isEmpty()) {
            System.out.println("No treatments found.");
        } else {
            System.out.println("\nList of Treatments:");
            for (Treatment t : treatments) {
                System.out.println("ID: " + t.getTreatmentID() + " | Type: " + t.getTreatmentType() + " | Price: $" + t.getPrice() + " | Length: " + t.getLength() + " mins");
            }
        }
    }

    private static void searchTreatment() {
        System.out.print("\nEnter Treatment ID to search: ");
        int id = getIntInput();
        Treatment treatment = treatmentCRUD.getTreatmentById(id);
        
        if (treatment != null) {
            System.out.println("\nTreatment Found:");
            System.out.println("ID: " + treatment.getTreatmentID());
            System.out.println("Type: " + treatment.getTreatmentType());
            System.out.println("Price: $" + treatment.getPrice());
            System.out.println("Length: " + treatment.getLength() + " minutes");
        } else {
            System.out.println("Treatment not found.");
        }
    }

    private static void updateTreatment() {
        System.out.print("\nEnter Treatment ID to update: ");
        int id = getIntInput();
        Treatment existingTreatment = treatmentCRUD.getTreatmentById(id);

        if (existingTreatment == null) {
            System.out.println("Treatment not found.");
            return;
        }

        System.out.println("\nUpdating Treatment Details (Leave blank to keep existing values):");
        System.out.print("New Treatment Type (" + existingTreatment.getTreatmentType() + "): ");
        String treatmentType = scanner.nextLine();
        if (treatmentType.isEmpty()) treatmentType = existingTreatment.getTreatmentType();

        System.out.print("New Price ($" + existingTreatment.getPrice() + "): ");
        String priceInput = scanner.nextLine();
        int price = priceInput.isEmpty() ? existingTreatment.getPrice() : Integer.parseInt(priceInput);

        System.out.print("New Length (" + existingTreatment.getLength() + " minutes): ");
        String lengthInput = scanner.nextLine();
        int length = lengthInput.isEmpty() ? existingTreatment.getLength() : Integer.parseInt(lengthInput);

        Treatment updatedTreatment = new Treatment(id, treatmentType, price, length);
        treatmentCRUD.updateTreatment(updatedTreatment);
        System.out.println("Treatment updated successfully!");
    }

    private static void deleteTreatment() {
        System.out.print("\nEnter Treatment ID to delete: ");
        int id = getIntInput();
        Treatment treatment = treatmentCRUD.getTreatmentById(id);

        if (treatment == null) {
            System.out.println("Treatment not found.");
            return;
        }

        System.out.print("Are you sure you want to delete " + treatment.getTreatmentType() + "? (yes/no): ");
        String confirmation = scanner.nextLine().toLowerCase();

        if (confirmation.equals("yes")) {
            treatmentCRUD.deleteTreatment(id);
            System.out.println("Treatment deleted successfully.");
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
        scanner.nextLine(); // Clear buffer
        return value;
    }
}