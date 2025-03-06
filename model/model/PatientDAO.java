package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/dentalsystem"; 
        String username = "root";
        String password = "password"; 
        return DriverManager.getConnection(url, username, password);
    }

    public void addPatient(Patient patient) {
        String query = "INSERT INTO patients (first_name, last_name, date_of_birth, email, street, town, county, eircode, medical_card) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
        String query = "SELECT * FROM patients";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Patient patient = new Patient(
                    resultSet.getInt("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("date_of_birth"),
                    resultSet.getString("email"),
                    resultSet.getString("street"),
                    resultSet.getString("town"),
                    resultSet.getString("county"),
                    resultSet.getString("eircode"),
                    resultSet.getBoolean("medical_card")
                );
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public Patient getPatientById(int id) {
        String query = "SELECT * FROM patients WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Patient(
                    resultSet.getInt("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("date_of_birth"),
                    resultSet.getString("email"),
                    resultSet.getString("street"),
                    resultSet.getString("town"),
                    resultSet.getString("county"),
                    resultSet.getString("eircode"),
                    resultSet.getBoolean("medical_card")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePatient(Patient patient) {
        String query = "UPDATE patients SET first_name = ?, last_name = ?, date_of_birth = ?, email = ?, street = ?, town = ?, county = ?, eircode = ?, medical_card = ? WHERE id = ?";
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

    public void deletePatient(int id) {
        String query = "DELETE FROM patients WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
