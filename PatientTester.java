import java.util.List;

public class PatientTester {

    public static void main(String[] args) {
        Patient.PatientDAO patientDAO = new Patient().new PatientDAO();

        Patient newPatient = new Patient(0, "John", "Doe", "1990-01-01", "john.doe@email.com", "123 Main St", "Dublin", "Dublin", "D01AB23", true);
        patientDAO.addPatient(newPatient);

        List<Patient> allPatients = patientDAO.getAllPatients();
        System.out.println("All Patients:");
        for (Patient patient : allPatients) {
            System.out.println("ID: " + patient.getId() + ", Name: " + patient.getFirstName() + " " + patient.getLastName());
        }

       
        int patientIdToRetrieve = 1;  
        Patient patient = patientDAO.getPatientById(patientIdToRetrieve);
        if (patient != null) {
            System.out.println("Patient with ID " + patientIdToRetrieve + ":");
            System.out.println("Name: " + patient.getFirstName() + " " + patient.getLastName());
            System.out.println("Email: " + patient.getEmail());
        } else {
            System.out.println("No patient found with ID " + patientIdToRetrieve);
        }

      
        patient.setFirstName("Jane");
        patient.setLastName("Doe");
        patient.setEmail("jane.doe@email.com");
        patientDAO.updatePatient(patient);

 
        int patientIdToDelete = 2; 
        patientDAO.deletePatient(patientIdToDelete);
    }
}