package tester;

import model.TreatmentCRUD; // Importing the TreatmentCRUD class from the model package
import model.TreatmentCRUD.Treatment; // Importing the Treatment class from the model package

import java.util.List;

public class TreatmentCRUDTester {

    public static void main(String[] args) {
        // Creating an instance of TreatmentCRUD class to test
        TreatmentCRUD treatmentCRUD = new TreatmentCRUD();

        // 1. Test Add Treatment
        System.out.println("Adding a new treatment...");
        Treatment newTreatment = new Treatment(0, "Teeth Whitening", 200, 60);
        treatmentCRUD.addTreatment(newTreatment);

        // 2. Test Get All Treatments
        System.out.println("\nFetching all treatments...");
        List<TreatmentCRUD.Treatment> treatments = treatmentCRUD.getAllTreatments();
        for (TreatmentCRUD.Treatment treatment : treatments) {
            System.out.println("ID: " + treatment.getTreatmentID() + ", Type: " + treatment.getTreatmentType() +
                    ", Price: " + treatment.getPrice() + ", Length: " + treatment.getLength() + " minutes");
        }

        // 3. Test Update Treatment
        System.out.println("\nUpdating treatment...");
        Treatment updatedTreatment = treatments.get(0);  // Take the first treatment
        updatedTreatment.setPrice(250);
        updatedTreatment.setLength(75);
        treatmentCRUD.updateTreatment(updatedTreatment);

        // 4. Test Get Treatment by ID
        System.out.println("\nFetching treatment by ID...");
        TreatmentCRUD.Treatment fetchedTreatment = treatmentCRUD.getTreatmentById(updatedTreatment.getTreatmentID());
        if (fetchedTreatment != null) {
            System.out.println("ID: " + fetchedTreatment.getTreatmentID() + ", Type: " + fetchedTreatment.getTreatmentType() +
                    ", Price: " + fetchedTreatment.getPrice() + ", Length: " + fetchedTreatment.getLength() + " minutes");
        }

    }
}