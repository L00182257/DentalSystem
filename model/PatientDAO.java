package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/dentistdb"; 
        String username = "root";
        String password = "password";
        return DriverManager.getConnection(url, username, password);
    }

    public void addPatient(Patient patient) {

        String query = "INSERT INTO Patient (FirstName, LastName, DateOfBirth, Email, Street, Town, County, Eircode, MedicalCard, AmountOwed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";        
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
            statement.setDouble(10, patient.getAmtOwed());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    patient.setPatientID(generatedKeys.getInt(1));
                    System.out.println("Patient added successfully! ID: " + patient.getPatientID());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM Patient";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Patient patient = new Patient(
                    resultSet.getInt("PatientID"),
                    resultSet.getString("FirstName"),
                    resultSet.getString("LastName"),
                    resultSet.getString("DateOfBirth"),
                    resultSet.getString("Email"),
                    resultSet.getString("Street"),
                    resultSet.getString("Town"),
                    resultSet.getString("County"),
                    resultSet.getString("Eircode"),
                    resultSet.getBoolean("MedicalCard"),
                    resultSet.getDouble("AmountOwed")
                );
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public Patient getPatientById(int patientID) {
        String query = "SELECT * FROM Patient WHERE PatientID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, patientID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Patient(
                    resultSet.getInt("PatientID"),
                    resultSet.getString("FirstName"),
                    resultSet.getString("LastName"),
                    resultSet.getString("DateOfBirth"),
                    resultSet.getString("Email"),
                    resultSet.getString("Street"),
                    resultSet.getString("Town"),
                    resultSet.getString("County"),
                    resultSet.getString("Eircode"),
                    resultSet.getBoolean("MedicalCard"),
                    resultSet.getDouble("Amount Owed")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePatient(Patient patient) {
        String query = "UPDATE Patient SET FirstName = ?, LastName = ?, DateOfBirth = ?, Email = ?, Street = ?, Town = ?, County = ?, Eircode = ?, MedicalCard = ?, AmountOwed = ? WHERE PatientID = ?";        try (Connection connection = getConnection();
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
            statement.setInt(10, patient.getPatientID());
            statement.setDouble(11, patient.getAmtOwed());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePatient(int patientID) {
        
        String deleteQuery = "DELETE FROM patient WHERE PatientID = ?";
        String maxIDQuery = "SELECT MAX(PatientID) FROM patient";
        String resetAutoIncrementQuery = "ALTER TABLE patient AUTO_INCREMENT = ?";
        
        try (Connection connection = getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
             Statement maxIDStatement = connection.createStatement()) {
    
           
            deleteStatement.setInt(1, patientID);
            deleteStatement.executeUpdate();
    
            
            ResultSet maxIDResult = maxIDStatement.executeQuery(maxIDQuery);
            if (maxIDResult.next()) {
                int maxID = maxIDResult.getInt(1);
    
                
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
