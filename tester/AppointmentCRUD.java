package model;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// import javafx.util.Callback;

public class AppointmentCRUD {

    // Database connection setup
    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/dentistdb"; // Change database name
        String username = "root"; // username
        String password = "TKJjoq2d."; // password
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
        private String patientFullName;
        private String dentistFullName;
        private String treatmentName;

        // No-argument constructor
        public Appointment() {
            this.dateOfAppointment = null;
            this.timeOfAppointment = null;
            this.attended = false;
            this.treatmentID = 0;
            this.patientID = 0;
            this.dentistID = 0;
        }

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
                    // New getters and setters
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

    // CREATE - Add a new appointment
    public void addAppointment(Appointment appointment) {
        LocalDateTime now = LocalDateTime.now(); // Get current date & time
        LocalDateTime appointmentDateTime = LocalDateTime.of(
            appointment.getDateOfAppointment().toLocalDate(),  // Convert SQL Date to LocalDate
            appointment.getTimeOfAppointment().toLocalTime()   // Convert SQL Time to LocalTime
        );

        if (appointmentDateTime.isBefore(now)) {
            System.out.println("Error: Cannot book an appointment in the past.");
            return; // Stop execution if the date/time is in the past
        }

        // Check if the dentist is already booked at this date & time
        if (isDentistBooked(appointment.getDentistID(), appointment.getDateOfAppointment(), appointment.getTimeOfAppointment())) {
            System.out.println("Error: This dentist is already booked for this time.");
            return;
        }

        String query = "INSERT INTO appointment (DateOfAppointment, TimeOfAppointment, Attended, TreatmentID, PatientID, DentistID) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setDate(1, appointment.getDateOfAppointment());
            statement.setTime(2, appointment.getTimeOfAppointment());
            statement.setBoolean(3, appointment.isAttended());
            statement.setInt(4, appointment.getTreatmentID());
            statement.setInt(5, appointment.getPatientID());
            statement.setInt(6, appointment.getDentistID());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } private boolean isDentistBooked(int dentistID, Date date, Time time) {
        String query = "SELECT COUNT(*) FROM appointment WHERE DentistID = ? AND DateOfAppointment = ? AND TimeOfAppointment = ?";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            statement.setInt(1, dentistID);
            statement.setDate(2, date);
            statement.setTime(3, time);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return true; // Dentist is already booked
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Dentist is available
    }
    
    /* 
    public void addAppointment(Appointment appointment) {
        // Convert java.sql.Date and java.sql.Time to LocalDate and LocalTime for validation
        LocalDate appointmentDate = appointment.getDateOfAppointment().toLocalDate();
        LocalTime appointmentTime = appointment.getTimeOfAppointment().toLocalTime();
        LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentTime);
    
        LocalDateTime now = LocalDateTime.now(); // Get current date & time
    
        // Validate that the appointment is not in the past
        if (appointmentDateTime.isBefore(now)) {
            System.out.println("Error: Cannot book an appointment in the past.");
            return; // Stop execution if the date/time is in the past
        }
    
        // Check if the dentist is already booked at this date & time
        if (isDentistBooked(appointment.getDentistID(), appointment.getDateOfAppointment(), appointment.getTimeOfAppointment())) {
            System.out.println("Error: This dentist is already booked for this time.");
            return;
        }
    
        // Insert the appointment into the database
        String query = "INSERT INTO appointment (DateOfAppointment, TimeOfAppointment, Attended, TreatmentID, PatientID, DentistID) VALUES (?, ?, ?, ?, ?, ?)";
    
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
    
            statement.setDate(1, appointment.getDateOfAppointment()); // Use java.sql.Date
            statement.setTime(2, appointment.getTimeOfAppointment()); // Use java.sql.Time
            statement.setBoolean(3, appointment.isAttended());
            statement.setInt(4, appointment.getTreatmentID());
            statement.setInt(5, appointment.getPatientID());
            statement.setInt(6, appointment.getDentistID());
    
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isDentistBooked(int dentistID, Date date, Time time) {
        String query = "SELECT COUNT(*) FROM appointment WHERE DentistID = ? AND DateOfAppointment = ? AND TimeOfAppointment = ?";
    
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            statement.setInt(1, dentistID);
            statement.setDate(2, date);
            statement.setTime(3, time);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return true; // Dentist is already booked
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Dentist is available
    }
    
*/
    // READ - Get all appointments
    // public List<Appointment> getAllAppointments() {
    //     List<Appointment> appointments = new ArrayList<>();
    //     String query = "SELECT * FROM appointment";

    //     try (Connection connection = getConnection();
    //          Statement statement = connection.createStatement();
    //          ResultSet resultSet = statement.executeQuery(query)) {

    //         while (resultSet.next()) {
    //             Appointment appointment = new Appointment(
    //                     resultSet.getDate("DateOfAppointment"),
    //                     resultSet.getTime("TimeOfAppointment"),
    //                     resultSet.getBoolean("Attended"),
    //                     resultSet.getInt("TreatmentID"),
    //                     resultSet.getInt("PatientID"),
    //                     resultSet.getInt("DentistID")
    //             );
    //             appointment.setAppointmentID(resultSet.getInt("AppointmentID"));
    //             appointments.add(appointment);
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }

    //     return appointments;
    // }
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT " +
                       "a.AppointmentID, a.DateOfAppointment, a.TimeOfAppointment, a.Attended, " +
                       "p.FirstName AS PatientFirstName, p.LastName AS PatientLastName, " +
                       "d.FirstName AS DentistFirstName, d.LastName AS DentistLastName, " +
                       "t.TreatmentType " +
                       "FROM Appointment a " +
                       "JOIN Patient p ON a.PatientID = p.PatientID " +
                       "JOIN Dentist d ON a.DentistID = d.DentistID " +
                       "JOIN Treatment t ON a.TreatmentID = t.TreatmentID";
    
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentID(rs.getInt("AppointmentID"));
                appointment.setDateOfAppointment(rs.getDate("DateOfAppointment")); // No conversion to LocalDate                
                appointment.setTimeOfAppointment(rs.getTime("TimeOfAppointment"));
                appointment.setAttended(rs.getBoolean("Attended"));
                appointment.setPatientFullName(rs.getString("PatientFirstName") + " " + rs.getString("PatientLastName"));
                appointment.setDentistFullName(rs.getString("DentistFirstName") + " " + rs.getString("DentistLastName"));
                appointment.setTreatmentName(rs.getString("TreatmentType"));
    
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

    // public Callback getPatients() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getPatients'");
    // }

    // public Callback getDentists() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getDentists'");
    // }
    public List<String> getPatients() {
        List<String> patients = new ArrayList<>();
        String query = "SELECT PatientID, CONCAT(FirstName, ' ', LastName) AS FullName FROM Patient";
    
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
    
            while (resultSet.next()) {
                int id = resultSet.getInt("PatientID");
                String fullName = resultSet.getString("FullName");
                patients.add(id + ": " + fullName); // Format: "ID: Name"
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return patients;
    }

  

  /*public List<String> getDentists() {
      DentistDAO dentistDAO = new DentistDAO();
      List<Dentist> dentists = dentistDAO.getAllDentists();
      System.out.println("Number of dentists fetched: " + dentists.size());  // Debugging line
      List<String> dentistStrings = new ArrayList<>();
      
      for(Dentist d : dentists) {
          dentistStrings.add(d.getDentistID() + ": " + 
                           d.getFirstName() + " " + 
                           d.getLastName());
      }
      return dentistStrings;
  }*/
  public List<String> getDentists() {
    List<String> dentists = new ArrayList<>();
    String query = "SELECT DentistID, CONCAT(FirstName, ' ', LastName) AS FullName FROM Dentist";

    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

        while (resultSet.next()) {
            int id = resultSet.getInt("DentistID"); 
            String fullName = resultSet.getString("FullName");
            dentists.add(id + ": " + fullName); // Format: "ID: Name"
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return dentists;
}

public List<String> getDentistsSorting() {
    List<String> dentists = new ArrayList<>();
    String query = "SELECT DentistID, CONCAT(FirstName, ' ', LastName) AS FullName FROM Dentist";

    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

        while (resultSet.next()) {
            String fullName = resultSet.getString("FullName");
            dentists.add(fullName); // Format: "ID: Name"
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return dentists;
}

  public List<String> getTreatments() {
      TreatmentCRUD treatmentDAO = new TreatmentCRUD();
      List<TreatmentCRUD.Treatment> treatments = treatmentDAO.getAllTreatments();
      List<String> treatmentStrings = new ArrayList<>();
      
      for(TreatmentCRUD.Treatment t : treatments) {
        treatmentStrings.add(t.getTreatmentID() + ": " + 
        t.getTreatmentType() + " (" + 
        t.getLength() + " mins)");
      }
      return treatmentStrings;
  }

  public Map<Integer, LocalDateTime> getNextAvailableAppointments() {
    Map<Integer, LocalDateTime> nextAvailableAppointments = new HashMap<>();

    String dentistQuery = "SELECT DentistID FROM Dentist";
    String appointmentQuery = "SELECT DateOfAppointment, TimeOfAppointment FROM appointment " +
                              "WHERE DentistID = ? AND DateOfAppointment >= CURDATE() " +
                              "ORDER BY DateOfAppointment ASC, TimeOfAppointment ASC";

    try (Connection connection = getConnection();
         PreparedStatement dentistStmt = connection.prepareStatement(dentistQuery);
         ResultSet dentistResult = dentistStmt.executeQuery()) {

        while (dentistResult.next()) {
            int dentistID = dentistResult.getInt("DentistID");
            LocalDateTime availableTime = findNextAvailableTime(connection, dentistID, appointmentQuery);
            nextAvailableAppointments.put(dentistID, availableTime);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return nextAvailableAppointments;
}
 
private LocalDateTime findNextAvailableTime(Connection connection, int dentistID, String query) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, dentistID);
        ResultSet rs = stmt.executeQuery();

        LocalDateTime now = LocalDateTime.now(); // Current time
        LocalDate today = now.toLocalDate();
        LocalTime currentTime = now.toLocalTime();

        LocalTime workStart = LocalTime.of(9, 0);  // Dentist starts at 09:00 AM
        LocalTime workEnd = LocalTime.of(17, 0);   // Dentist ends at 5:00 PM
        Duration slotDuration = Duration.ofMinutes(30); // Each appointment lasts 30 minutes

        // 🔹 Determine the first possible time for today
        LocalTime firstPossibleTime;
        if (currentTime.isBefore(workStart)) {
            firstPossibleTime = workStart;
        } else if (currentTime.isAfter(workEnd)) {
            today = today.plusDays(1); // Move to next day
            firstPossibleTime = workStart;
        } else {
            // Round current time to next available 30-minute slot
            int minutes = currentTime.getMinute();
            int roundedMinutes = (minutes / 30) * 30 + 30;
            if (roundedMinutes >= 60) {
                firstPossibleTime = LocalTime.of(currentTime.getHour() + 1, 0);
            } else {
                firstPossibleTime = LocalTime.of(currentTime.getHour(), roundedMinutes);
            }
            // Ensure it's within working hours
            if (firstPossibleTime.isAfter(workEnd)) {
                today = today.plusDays(1);
                firstPossibleTime = workStart;
            }
        }

        LocalDateTime nextAvailableTime = LocalDateTime.of(today, firstPossibleTime);

        // 🔍 Check against existing appointments
        while (rs.next()) {
            LocalDate appointmentDate = rs.getDate("DateOfAppointment").toLocalDate();
            LocalTime appointmentTime = rs.getTime("TimeOfAppointment").toLocalTime();
            LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentTime);

            // If the suggested time conflicts, move to the next slot
            if (!nextAvailableTime.isBefore(appointmentDateTime) && nextAvailableTime.equals(appointmentDateTime)) {
                nextAvailableTime = nextAvailableTime.plus(slotDuration);

                // If next slot goes past work hours, move to the next day
                if (nextAvailableTime.toLocalTime().isAfter(workEnd)) {
                    today = today.plusDays(1);
                    nextAvailableTime = LocalDateTime.of(today, workStart);
                }
            }
        }

        return nextAvailableTime;
    }
}

public List<Appointment> getAppointmentsByDentistFullName(String dentistFullName) {
    List<Appointment> appointments = new ArrayList<>();
    String query = "SELECT " +
                   "a.AppointmentID, a.DateOfAppointment, a.TimeOfAppointment, a.Attended, " +
                   "p.FirstName AS PatientFirstName, p.LastName AS PatientLastName, " +
                   " d.FirstName AS DentistFirstName, d.LastName AS DentistLastName, " +
                   "t.TreatmentType " +
                   "FROM Appointment a " +
                   "JOIN Patient p ON a.PatientID = p.PatientID " +
                   "JOIN Dentist d ON a.DentistID = d.DentistID " +
                   "JOIN Treatment t ON a.TreatmentID = t.TreatmentID " +
                   "WHERE CONCAT(d.FirstName, ' ', d.LastName) = ?";

    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, dentistFullName);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentID(resultSet.getInt("AppointmentID"));
                appointment.setDateOfAppointment(resultSet.getDate("DateOfAppointment"));
                appointment.setTimeOfAppointment(resultSet.getTime("TimeOfAppointment"));
                appointment.setAttended(resultSet.getBoolean("Attended"));
                appointment.setPatientFullName(resultSet.getString("PatientFirstName") + " " + resultSet.getString("PatientLastName"));
                appointment.setDentistFullName(resultSet.getString("DentistFirstName") + " " + resultSet.getString("DentistLastName"));
                appointment.setTreatmentName(resultSet.getString("TreatmentType"));

                appointments.add(appointment);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return appointments;
}

public List<Appointment> getAppointmentsByDate(LocalDate date) {
    List<Appointment> appointments = new ArrayList<>();
    String query = "SELECT " +
                   "a.AppointmentID, a.DateOfAppointment, a.TimeOfAppointment, a.Attended, " +
                   "p.FirstName AS PatientFirstName, p.LastName AS PatientLastName, " +
                   "d.FirstName AS DentistFirstName, d.LastName AS DentistLastName, " +
                   "t.TreatmentType " +  // Fixed column name
                   "FROM Appointment a " +
                   "JOIN Patient p ON a.PatientID = p.PatientID " +
                   "JOIN Dentist d ON a.DentistID = d.DentistID " +
                   "JOIN Treatment t ON a.TreatmentID = t.TreatmentID " +
                   "WHERE a.DateOfAppointment = ?"; // Corrected WHERE condition

    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setDate(1, Date.valueOf(date));
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentID(resultSet.getInt("AppointmentID"));
                appointment.setDateOfAppointment(resultSet.getDate("DateOfAppointment"));
                appointment.setTimeOfAppointment(resultSet.getTime("TimeOfAppointment"));
                appointment.setAttended(resultSet.getBoolean("Attended"));
                appointment.setPatientFullName(resultSet.getString("PatientFirstName") + " " + resultSet.getString("PatientLastName"));
                appointment.setDentistFullName(resultSet.getString("DentistFirstName") + " " + resultSet.getString("DentistLastName"));
                appointment.setTreatmentName(resultSet.getString("TreatmentType")); // Fixed column name

                appointments.add(appointment);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return appointments;
}



    public String getDentistDetails(int dentistId) {
        String query = "SELECT DentistID, FirstName, LastName FROM dentist WHERE DentistID = ?";
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, dentistId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                return dentistId + ": "+ firstName + " " + lastName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown Dentist";
    }

    public String getPatientDetails(int patientId) {
        String query = "SELECT PatientID, FirstName, LastName, DOB FROM Patient WHERE PatientID = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // return String.format("%d - %s %s (%s)",
                //         resultSet.getInt("PatientID"),
                //         resultSet.getString("FirstName"),
                //         resultSet.getString("LastName"),
                //         resultSet.getDate("DOB"));
    
                        String firstName = resultSet.getString("FirstName");
                        String lastName = resultSet.getString("LastName");
                        return  firstName + " " + lastName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown Patient";
    }
    
    public String getTreatmentDetails(int treatmentId) {
        String query = "SELECT TreatmentID, TreatmentType, Length FROM Treatment WHERE TreatmentID = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, treatmentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // return String.format("%d - %s (%d mins)",
                //         resultSet.getInt("TreatmentID"),
                //         resultSet.getString("TreatmentType"),
                //         resultSet.getInt("Length"));
    
                String treatmentType = resultSet.getString("TreatmentType");
                return treatmentType;
    
    
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown Treatment";
    }

    public List<Appointment> getAppointmentsByDateAndDentist(LocalDate date, String dentistFullName) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT " +
                       "a.AppointmentID, a.DateOfAppointment, a.TimeOfAppointment, a.Attended, " +
                       "p.FirstName AS PatientFirstName, p.LastName AS PatientLastName, " +
                       "d.FirstName AS DentistFirstName, d.LastName AS DentistLastName, " +
                       "t.TreatmentType " +
                       "FROM Appointment a " +
                       "JOIN Patient p ON a.PatientID = p.PatientID " +
                       "JOIN Dentist d ON a.DentistID = d.DentistID " +
                       "JOIN Treatment t ON a.TreatmentID = t.TreatmentID " +
                       "WHERE a.DateOfAppointment = ? AND CONCAT(d.FirstName, ' ', d.LastName) = ?";
    
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            statement.setDate(1, java.sql.Date.valueOf(date));
            statement.setString(2, dentistFullName);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setAppointmentID(resultSet.getInt("AppointmentID"));
                    appointment.setDateOfAppointment(resultSet.getDate("DateOfAppointment"));
                    appointment.setTimeOfAppointment(resultSet.getTime("TimeOfAppointment"));
                    appointment.setAttended(resultSet.getBoolean("Attended"));
                    appointment.setPatientFullName(resultSet.getString("PatientFirstName") + " " + resultSet.getString("PatientLastName"));
                    appointment.setDentistFullName(resultSet.getString("DentistFirstName") + " " + resultSet.getString("DentistLastName"));
                    appointment.setTreatmentName(resultSet.getString("TreatmentType"));
    
                    appointments.add(appointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return appointments;
    }
}
