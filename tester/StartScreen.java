package tester;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StartScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dental Booking System");

        Text title = new Text("DENTAL BOOKING SYSTEM");
        title.setFont(Font.font("Arial", 28));
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-weight: bold; ");

        Button receptionistButton = createStyledButton("Receptionist");
        receptionistButton.setOnAction(e -> openNewWindow("Receptionist", new ReceptionistScreen()));

        Button dentistButton = createStyledButton("Dentist");
        dentistButton.setOnAction(e -> openNewWindow("Dentist", new DentistScreen()));

        VBox layout = new VBox(20, title, receptionistButton, dentistButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2C3E50; -fx-padding: 50;");

        receptionistButton.setMaxWidth(Double.MAX_VALUE);
        dentistButton.setMaxWidth(Double.MAX_VALUE);

        Scene scene = new Scene(layout, 450, 350);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start checking appointments in the background
        startAppointmentChecker();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: #3498DB; " +  
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #2980B9; " + 
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #3498DB; " + 
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        ));
        return button;
    }

    // ✅ Opens Receptionist or Dentist in a new window
    private void openNewWindow(String title, Application app) {
        Stage newStage = new Stage();
        newStage.setTitle(title);
        try {
            app.start(newStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        newStage.initModality(Modality.NONE); // Allows interaction with StartScreen
        newStage.show();
    }

    private void startAppointmentChecker() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(TimeChecker::checkAppointments, 0, 1, TimeUnit.MINUTES);
    }

    // === TIME CHECKER (Now inside StartScreen) ===
    private static class TimeChecker {
        private static Connection getConnection() throws SQLException {
            String url = "jdbc:mysql://localhost:3306/dentistdb"; // Change if needed
            String username = "root"; // Database username
            String password = "password"; // Database password
            return DriverManager.getConnection(url, username, password);
        }

        public static void checkAppointments() {
            String query = "SELECT AppointmentID, PatientID, DateOfAppointment, TimeOfAppointment FROM Appointment";

            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    int appointmentId = rs.getInt("AppointmentID");
                    int patientId = rs.getInt("PatientID");
                    LocalDate date = rs.getDate("DateOfAppointment").toLocalDate();
                    LocalTime time = rs.getTime("TimeOfAppointment").toLocalTime();
                    LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);
                    LocalDateTime currentTime = LocalDateTime.now();

                    // Check if 30 minutes have passed
                    long minutesElapsed = ChronoUnit.MINUTES.between(appointmentDateTime, currentTime);
                    if (minutesElapsed >= 30) {
                        updateAmountOwedAndDeleteAppointment(patientId, appointmentId);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private static void updateAmountOwedAndDeleteAppointment(int patientId, int appointmentId) {
            String updateQuery = "UPDATE Patient SET AmountOwed = AmountOwed + 20 WHERE PatientID = ?";
            String deleteQuery = "DELETE FROM Appointment WHERE AppointmentID = ?";

            try (Connection conn = getConnection()) {
                conn.setAutoCommit(false);

                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                     PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {

                    updateStmt.setInt(1, patientId);
                    int rowsUpdated = updateStmt.executeUpdate();

                    deleteStmt.setInt(1, appointmentId);
                    int rowsDeleted = deleteStmt.executeUpdate();

                    if (rowsUpdated > 0 && rowsDeleted > 0) {
                        conn.commit();
                        System.out.println("Charged PatientID: " + patientId + " for missing appointment. Appointment deleted.");
                    } else {
                        conn.rollback();
                        System.out.println("Failed to update patient charge or delete appointment.");
                    }
                } catch (SQLException e) {
                    conn.rollback();
                    e.printStackTrace();
                } finally {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}