package model;

import java.sql.*;
import java.util.*;

public class TreatmentCRUD {
    // Database connection setup
    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/dentistdb";
        String username = "root"; // database username
        String password = "password";

        return DriverManager.getConnection(url, username, password);
    }

    // Treatment Model Class
    public static class Treatment {
        private int treatmentID;
        private String treatmentType;
        private int price;
        private int length;

        // Constructor
        public Treatment(int treatmentID, String treatmentType, int price, int length) {
            this.treatmentID = treatmentID;
            this.treatmentType = treatmentType;
            this.price = price;
            this.length = length;
        }

        // Getters and Setters
        public int getTreatmentID() {
            return treatmentID;
        }

        public void setTreatmentID(int treatmentID) {
            this.treatmentID = treatmentID;
        }

        public String getTreatmentType() {
            return treatmentType;
        }

        public void setTreatmentType(String treatmentType) {
            this.treatmentType = treatmentType;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }
    }

    // Create a new treatment
    public void addTreatment(Treatment treatment) {
        String query = "INSERT INTO treatment (treatmentType, price, length) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, treatment.getTreatmentType());
            statement.setInt(2, treatment.getPrice());
            statement.setInt(3, treatment.getLength());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Treatment added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve all treatments
    public List<Treatment> getAllTreatments() {
        List<Treatment> treatments = new ArrayList<>();
        String query = "SELECT * FROM treatment";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Treatment treatment = new Treatment(
                        resultSet.getInt("treatmentID"),
                        resultSet.getString("treatmentType"),
                        resultSet.getInt("price"),
                        resultSet.getInt("length")
                );
                treatment.setTreatmentID(resultSet.getInt("treatmentID"));
                treatments.add(treatment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return treatments;
    }

    // Update an existing treatment
    public void updateTreatment(Treatment treatment) {
        String query = "UPDATE treatment SET treatmentType = ?, price = ?, length = ? WHERE treatmentID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, treatment.getTreatmentType());
            statement.setInt(2, treatment.getPrice());
            statement.setInt(3, treatment.getLength());
            statement.setInt(4, treatment.getTreatmentID());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Treatment updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a treatment by ID
    public void deleteTreatment(int treatmentID) {
        String query = "DELETE FROM treatment WHERE treatmentID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, treatmentID);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Treatment deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Search Treatment by ID
    public Treatment getTreatmentById(int treatmentID) {
        String query = "SELECT * FROM treatment WHERE treatmentID = ?";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, treatmentID);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return new Treatment(
                    resultSet.getInt("treatmentID"),
                    resultSet.getString("treatmentType"),
                    resultSet.getInt("price"),
                    resultSet.getInt("length")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
