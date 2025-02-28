package model;

import java.sql.*;
import java.util.*;
import model.Patient;

// Patient DAO Class
public class PatientCRUD {
    // Database connection setup
    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/dentistdb";
        String username = "root";
        String password = "password";
        return DriverManager.getConnection(url, username, password);
    }

    // Add a new patient
    public void addPatient(Patient patient) {
        String query = "INSERT INTO patient (firstName, lastName, dateOfBirth, email, street, town, county, eircode, MedicalCard) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getDOB());
            statement.setString(4, patient.getEmail());
            statement.setString(5, patient.getStreet());
            statement.setString(6, patient.getTown());
            statement.setString(7, patient.getCounty());
            statement.setString(8, patient.getEircode());
            statement.setBoolean(9, patient.getMedCard());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    patient.setID(generatedKeys.getInt(1));
                    System.out.println("Patient added successfully! ID: " + patient.getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve all patients
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patient";
        
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Patient patient = new Patient(
                    resultSet.getInt("PatientID"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("dateOfBirth"),
                    resultSet.getString("email"),
                    resultSet.getString("street"),
                    resultSet.getString("town"),
                    resultSet.getString("county"),
                    resultSet.getString("eircode"),
                    resultSet.getBoolean("MedicalCard")
                );
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    // Search patient by ID
    public Patient getPatientById(int id) {
        String query = "SELECT * FROM patient WHERE PatientID = ?";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return new Patient(
                    resultSet.getInt("PatientID"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("dateOfBirth"),
                    resultSet.getString("email"),
                    resultSet.getString("street"),
                    resultSet.getString("town"),
                    resultSet.getString("county"),
                    resultSet.getString("eircode"),
                    resultSet.getBoolean("MedicalCard")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update patient details
    public void updatePatient(Patient patient) {
        String query = "UPDATE patient SET firstName = ?, lastName = ?, dateOfBirth = ?, email = ?, street = ?, town = ?, county = ?, eircode = ?, MedicalCard = ? WHERE PatientID = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getDOB());
            statement.setString(4, patient.getEmail());
            statement.setString(5, patient.getStreet());
            statement.setString(6, patient.getTown());
            statement.setString(7, patient.getCounty());
            statement.setString(8, patient.getEircode());
            statement.setBoolean(9, patient.getMedCard());
            statement.setInt(10, patient.getId());
            
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean doesPatientExist(int patientID) {
        String query = "SELECT COUNT(*) FROM patient WHERE PatientID = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete a patient by ID
    public void deletePatient(int patientID) {
        String deleteQuery = "DELETE FROM patient WHERE patientID = ?";
        String maxIDQuery = "SELECT MAX(patientID) FROM patient";
        String resetAutoIncrementQuery = "ALTER TABLE patient AUTO_INCREMENT = ?";
    
        try (Connection connection = getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
             Statement maxIDStatement = connection.createStatement()) {
    
            // Delete the dentist with the provided ID
            deleteStatement.setInt(1,patientID);
            deleteStatement.executeUpdate();
    
            // Retrieve the current maximum dentistID in the table
            ResultSet maxIDResult = maxIDStatement.executeQuery(maxIDQuery);
            if (maxIDResult.next()) {
                int maxID = maxIDResult.getInt(1);
    
                // If the maxID is lower than the dentistID that was deleted, reset the AUTO_INCREMENT
                if (maxID < patientID) {
                    int newAutoIncrementValue = patientID;
                    PreparedStatement resetAutoIncrementStatement = connection.prepareStatement(resetAutoIncrementQuery);
                    resetAutoIncrementStatement.setInt(1, newAutoIncrementValue);
                    resetAutoIncrementStatement.executeUpdate();
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
