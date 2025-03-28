package model;

// Dentist Model Class
public class Dentist {
    private int dentistID;
    private String lastName;
    private String firstName;
    private String awardingBody;
    private String specialty;
    private String dateOfBirth;
    private int phoneNo;
    private String patientFullName;
    private String dentistFullName;
    private String treatmentName;


    // Constructor
    public Dentist(int dentistID, String lastName, String firstName, String awardingBody, String specialty, String dateOfBirth, int phoneNo) {
        this.dentistID = dentistID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.awardingBody = awardingBody;
        this.specialty = specialty;
        this.dateOfBirth = dateOfBirth;
        this.phoneNo = phoneNo;
    }

    // Getters and Setters
    public int getDentistID() {
         return dentistID; 
        }

    public void setDentistID(int dentistID) {
         this.dentistID = dentistID; 
        }
        
    
    public String getLastName() {
         return lastName; 
        }

    public void setLastName(String lastName) {
         this.lastName = lastName; 
        }

    
    public String getFirstName() {
         return firstName; 
        }

    public void setFirstName(String firstName) {
         this.firstName = firstName; 
        }

    
    public String getAwardingBody() {
         return awardingBody; 
        }

    public void setAwardingBody(String awardingBody) {
         this.awardingBody = awardingBody; 
        }

    
    public String getSpecialty() {
         return specialty; 
        }

    public void setSpecialty(String specialty) {
         this.specialty = specialty; 
        }

    
    public String getDateOfBirth() {
         return dateOfBirth; 
        }

    public void setDateOfBirth(String dateOfBirth) {
         this.dateOfBirth = dateOfBirth; 
        }

    
    public int getPhoneNo() {
         return phoneNo; 
        }

    public void setPhoneNo(int phoneNo) {
         this.phoneNo = phoneNo; 
        }

        public String getPatientFullName() {
            return patientFullName;
        }
    
        public void setPatientFullName(String patientFullName) {
            this.patientFullName = patientFullName;
        }
    
        public String getDentistFullName() {
            return dentistFullName;
        }
    
        public void setDentistFullName(String dentistFullName) {
            this.dentistFullName = dentistFullName;
        }
    
        public String getTreatmentName() {
            return treatmentName;
        }
    
        public void setTreatmentName(String treatmentName) {
            this.treatmentName = treatmentName;
        }
}
