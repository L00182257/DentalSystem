package model;
import java.util.function.Function;
import java.util.regex.Pattern;


import javafx.scene.control.TextField;

public class Validation {
   // Common patterns
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z'-]{2,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s,'.-]{3,100}$");
    
    // Phone number pattern (supports Irish phone numbers)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+353|0)[1-9][0-9]{7,8}$");
    
    // Eircode pattern (Irish postal code)
    private static final Pattern EIRCODE_PATTERN = Pattern.compile("^[A-Z0-9]{3}\\s?[A-Z0-9]{4}$");
    
    // Date of Birth pattern (yyyy-mm-dd)
    private static final Pattern DOB_PATTERN = Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
    
    // Amount pattern (positive numbers with optional 2 decimal places)
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^\\d+\\.?\\d{0,2}$");

    // Patient validations
    public static boolean validatePatient(Patient patient) {
        return validateName(patient.getFirstName()) 
            && validateName(patient.getLastName())
            && validateDateOfBirth(patient.getDOB())
            && validateEmail(patient.getEmail())
            && validatePhone(patient.getPhoneNo()) // assuming you add phone field
            && validateStreet(patient.getStreet())
            && validateTown(patient.getTown())
            && validateCounty(patient.getCounty())
            && validateEircode(patient.getEircode())
            && validateAmount(patient.getAmtOwed());
    }

    // Shared validation methods
    public static boolean validateName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }
    
    public static boolean validateEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean validatePhone(String phone) {
        if (phone == null) return false;
        // Remove any whitespace or dashes for validation
        String cleanedPhone = phone.replaceAll("[\\s-]", "");
        return PHONE_PATTERN.matcher(cleanedPhone).matches();
    }
    
    public static boolean validateDateOfBirth(String dob) {
        if (dob == null || !DOB_PATTERN.matcher(dob).matches()) {
            return false;
        }
        
        // Additional validation for actual date validity
        try {
            String[] parts = dob.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            
            // Simple date validation (doesn't account for leap years)
            if (month == 2 && day > 29) return false;
            if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 31) return false;
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean validateEircode(String eircode) {
        if (eircode == null) return false;
        // Standardize format by removing space if present
        String cleanedEircode = eircode.replace(" ", "").toUpperCase();
        return EIRCODE_PATTERN.matcher(cleanedEircode).matches();
    }
    
    public static boolean validateStreet(String street) {
        return street != null && ADDRESS_PATTERN.matcher(street).matches();
    }
    
    public static boolean validateTown(String town) {
        return town != null && ADDRESS_PATTERN.matcher(town).matches();
    }
    
    public static boolean validateCounty(String county) {
        return county != null && ADDRESS_PATTERN.matcher(county).matches();
    }
    
    public static boolean validateAmount(double amount) {
        return amount >= 0;
    }
    
    public static boolean validateAmount(String amountStr) {
        if (amountStr == null) return false;
        try {
            double amount = Double.parseDouble(amountStr);
            return amount >= 0 && AMOUNT_PATTERN.matcher(amountStr).matches();
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
  

}