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
        String query = "INSERT INTO Patient (FirstName, LastName, DateOfBirth, PhoneNo, Email, Street, Town, County, Eircode, MedicalCard, AmountOwed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getDOB());
            statement.setString(4, patient.getPhoneNo());
            statement.setString(5, patient.getEmail());
            statement.setString(6, patient.getStreet());
            statement.setString(7, patient.getTown());
            statement.setString(8, patient.getCounty());
            statement.setString(9, patient.getEircode());
            statement.setBoolean(10, patient.getMedCard());
            statement.setDouble(11, patient.getAmtOwed());

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
                        resultSet.getString("PhoneNo"), // Updated to String
                        resultSet.getString("Email"),
                        resultSet.getString("Street"),
                        resultSet.getString("Town"),
                        resultSet.getString("County"),
                        resultSet.getString("Eircode"),
                        resultSet.getBoolean("MedicalCard"),
                        resultSet.getDouble("AmountOwed"));
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
                        resultSet.getString("PhoneNo"), // Updated to String
                        resultSet.getString("Email"),
                        resultSet.getString("Street"),
                        resultSet.getString("Town"),
                        resultSet.getString("County"),
                        resultSet.getString("Eircode"),
                        resultSet.getBoolean("MedicalCard"),
                        resultSet.getDouble("AmountOwed"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePatient(Patient patient) {
        String query = "UPDATE Patient SET FirstName = ?, LastName = ?, DateOfBirth = ?, PhoneNo = ?, Email = ?, Street = ?, Town = ?, County = ?, Eircode = ?, MedicalCard = ?, AmountOwed = ? WHERE PatientID = ?";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getDOB());
            statement.setString(4, patient.getPhoneNo()); // Updated to String
            statement.setString(5, patient.getEmail());
            statement.setString(6, patient.getStreet());
            statement.setString(7, patient.getTown());
            statement.setString(8, patient.getCounty());
            statement.setString(9, patient.getEircode());
            statement.setBoolean(10, patient.getMedCard());
            statement.setDouble(11, patient.getAmtOwed());
            statement.setInt(12, patient.getPatientID());

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

        try (Connection connection = getConnection();
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {

            deleteStatement.setInt(1, patientID);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
