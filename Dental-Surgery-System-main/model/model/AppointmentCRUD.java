package model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentCRUD {

    // Database connection setup
    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/dentistdb"; // Change database name
        String username = "root"; // username
        String password = "password"; // password
        return DriverManager.getConnection(url, username, password);
    }

    // Appointment Model Class
    public static class Appointment {
        private int appointmentID;
        private Date dateOfAppointment;
        private Time timeOfAppointment;
        private boolean attended;
        private final int treatmentID;
        private final int patientID;
        private final int dentistID;

        // Constructor
        public Appointment(Date dateOfAppointment, Time timeOfAppointment, boolean attended, int treatmentID, int patientID, int dentistID) 
        {
            this.dateOfAppointment = dateOfAppointment;
            this.timeOfAppointment = timeOfAppointment;
            this.attended = attended;
            this.treatmentID = treatmentID;
            this.patientID = patientID;
            this.dentistID = dentistID;
        }

        // Getters and setters (No setters for foreign keys)
        public int getAppointmentID() 
        { 
          return appointmentID; 
        }
        public void setAppointmentID(int appointmentID) 
        { 
          this.appointmentID = appointmentID; 
        }

        public Date getDateOfAppointment() 
        { 
          return dateOfAppointment; 
        }
        public void setDateOfAppointment(Date dateOfAppointment)
        { 
          this.dateOfAppointment = dateOfAppointment; 
        }

        public Time getTimeOfAppointment() 
        { 
          return timeOfAppointment;
        }
        public void setTimeOfAppointment(Time timeOfAppointment) 
        {
          this.timeOfAppointment = timeOfAppointment; 
        }

        public boolean isAttended() 
        { 
          return attended; 
        }
        public void setAttended(boolean attended) 
        {
          this.attended = attended;
        }

        public int getTreatmentID() 
        { 
          return treatmentID;
        }
        public int getPatientID() 
        {
          return patientID; 
        }
        public int getDentistID() 
        { 
          return dentistID; 
        }
    }

    // CREATE - Add a new appointment
    public void addAppointment(Appointment appointment) {
      String query = "INSERT INTO appointment (DateOfAppointment, TimeOfAppointment, Attended, TreatmentID, PatientID, DentistID) VALUES (?, ?, ?, ?, ?, ?)";
  
      try (Connection connection = getConnection();
           PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
  
          statement.setDate(1, appointment.getDateOfAppointment());
          statement.setTime(2, appointment.getTimeOfAppointment());
          statement.setBoolean(3, appointment.isAttended());
          statement.setInt(4, appointment.getTreatmentID());
          statement.setInt(5, appointment.getPatientID());  // Corrected the column name
          statement.setInt(6, appointment.getDentistID());
  
          int rowsAffected = statement.executeUpdate();
          if (rowsAffected > 0) {
              System.out.println("Appointment added successfully!");
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }

    // READ - Get all appointments
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointment";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getDate("DateOfAppointment"),
                        resultSet.getTime("TimeOfAppointment"),
                        resultSet.getBoolean("Attended"),
                        resultSet.getInt("TreatmentID"),
                        resultSet.getInt("PatientID"),
                        resultSet.getInt("DentistID")
                );
                appointment.setAppointmentID(resultSet.getInt("AppointmentID"));
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    // UPDATE - Modify an existing appointment (Only date, time, and attendance)
    public void updateAppointment(Appointment appointment) {
        String query = "UPDATE appointment SET DateOfAppointment = ?, TimeOfAppointment = ?, Attended = ? WHERE AppointmentID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDate(1, appointment.getDateOfAppointment());
            statement.setTime(2, appointment.getTimeOfAppointment());
            statement.setBoolean(3, appointment.isAttended());
            statement.setInt(4, appointment.getAppointmentID());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE - Remove an appointment by ID
    public void deleteAppointment(int appointmentID) {
        String query = "DELETE FROM appointment WHERE AppointmentID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, appointmentID);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}