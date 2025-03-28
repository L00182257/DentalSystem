package model;

public class Patient{
    private int patientID;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
    private String street;
    private String town;
    private String county;
    private String eircode;
    private Boolean medCard;
    private Double amtOwed;

    public Patient(int patientID,String firstName, String lastName, String dateOfBirth, String email, String street, String town, String county, String eircode, Boolean medCard, Double amtOwed) {
        this.patientID = patientID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.street = street;
        this.town = town;
        this.county = county;
        this.eircode = eircode;
        this.medCard = medCard;
        this.amtOwed = amtOwed; 
    }
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDOB(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setEircode(String eircode) {
        this.eircode = eircode;
    }

    public void setAmtOwed(Double amtOwed) {
        this.amtOwed = amtOwed;
    }

    public void setMedCard(Boolean medCard) {
        this.medCard = medCard;
    }

    public int getPatientID() {
        return patientID; 
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDOB() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getStreet() {
        return street;
    }

    public String getTown() {
        return town;
    }

    public String getCounty() {
        return county;
    }

    public String getEircode() {
        return eircode;
    }

    public Boolean getMedCard() {
        return medCard;
    }

    public Double getAmtOwed() {
        return amtOwed;
    }
    @Override
    public String toString() {
        return "Patient{" +
           "id=" + patientID +
           ", firstName='" + firstName + '\'' +
           ", lastName='" + lastName + '\'' +
           ", dateOfBirth='" + dateOfBirth + '\'' +
           ", email='" + email + '\'' +
           ", street='" + street + '\'' +
           ", town='" + town + '\'' +
           ", county='" + county + '\'' +
           ", eircode='" + eircode + '\'' +
           ", medCard=" + medCard + '\'' +
           ", amtOwed=" + amtOwed +
           '}';
}
}
