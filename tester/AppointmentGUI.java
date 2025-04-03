package tester;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import model.AppointmentCRUD;
import model.AppointmentCRUD.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class AppointmentGUI extends Application {
    private TableView<Appointment> table;
    private AppointmentCRUD appointmentDAO = new AppointmentCRUD();
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Appointment Management System");
        setupTable();
        setupMainLayout(primaryStage);
    }

    @SuppressWarnings("unchecked")
    private void setupTable() {
        table = new TableView<>();
        
        TableColumn<Appointment, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));

        // TableColumn<Appointment, Integer> patientColumn = new TableColumn<>("Patient");
        // patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));

        // TableColumn<Appointment, Integer> dentistColumn = new TableColumn<>("Dentist");
        // dentistColumn.setCellValueFactory(new PropertyValueFactory<>("dentistID"));

        // TableColumn<Appointment, Integer> treatmentColumn = new TableColumn<>("Treatment");
        // treatmentColumn.setCellValueFactory(new PropertyValueFactory<>("treatmentID"));

            // Updated columns for names
        TableColumn<Appointment, String> patientColumn = new TableColumn<>("Patient");
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientFullName"));

        TableColumn<Appointment, String> dentistColumn = new TableColumn<>("Dentist");
        dentistColumn.setCellValueFactory(new PropertyValueFactory<>("dentistFullName"));

        TableColumn<Appointment, String> treatmentColumn = new TableColumn<>("Treatment");
        treatmentColumn.setCellValueFactory(new PropertyValueFactory<>("treatmentName"));
            
        TableColumn<Appointment, Date> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfAppointment"));

        TableColumn<Appointment, Time> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfAppointment"));

        TableColumn<Appointment, Boolean> attendedColumn = new TableColumn<>("Attended");
        attendedColumn.setCellValueFactory(new PropertyValueFactory<>("attended"));

        table.getColumns().addAll(idColumn, patientColumn, dentistColumn, treatmentColumn, dateColumn, timeColumn, attendedColumn);
        refreshTable();
    }
    
    private void setupMainLayout(Stage primaryStage) {
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15));

        Button nextAvailableButton = createStyledButton("Next Available Appointment");
        nextAvailableButton.setOnAction(e -> showNextAvailableAppointments());

        Button addButton = createStyledButton("Add Appointment");
        addButton.setOnAction(e -> showAppointmentForm(null));

        Button updateButton = createStyledButton("Update Appointment");
        updateButton.setOnAction(e -> handleUpdate());

        Button deleteButton = createStyledButton("Delete Appointment");
        deleteButton.setOnAction(e -> deleteAppointment());

        Button refreshButton = createStyledButton("Refresh");
        refreshButton.setOnAction(e -> refreshTable());

        Button backToStartScreen = createStyledButton("Back");
        backToStartScreen.setOnAction(e -> backToStartScreen());

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, refreshButton, nextAvailableButton, backToStartScreen);

        VBox layout = new VBox(15, table, buttonBox);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2C3E50; -fx-padding: 20px;");

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void showAppointmentFormWithPreFilledData(int dentistId, LocalDateTime availableTime) {
        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.setTitle("Create Appointment");
    
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
    
        // Form components
         // Form components with search functionality
         ComboBox<String> patientPicker = createSearchableComboBox(appointmentDAO.getPatients());
         ComboBox<String> treatmentPicker = createSearchableComboBox(appointmentDAO.getTreatments());
        DatePicker datePicker = new DatePicker(availableTime.toLocalDate());
        ComboBox<String> timePicker = new ComboBox<>(
            FXCollections.observableArrayList(availableTime.toLocalTime().toString()));
        timePicker.setValue(availableTime.toLocalTime().toString());
        CheckBox attendedCheck = new CheckBox("Attended");
    
        Label dentistLabel = new Label(appointmentDAO.getDentistDetails(dentistId));
    
        // Add components to grid
        grid.addRow(0, new Label("Patient:"), patientPicker);
        grid.addRow(1, new Label("Dentist:"), dentistLabel);
        grid.addRow(2, new Label("Treatment:"), treatmentPicker);
        grid.addRow(3, new Label("Date:"), datePicker);
        grid.addRow(4, new Label("Time:"), timePicker);
        grid.addRow(5, attendedCheck);
    
        Button saveButton = createStyledButton("Save");
        saveButton.setOnAction(e -> handleSave(
            null, // New appointment
            patientPicker.getValue(),
            appointmentDAO.getDentistDetails(dentistId), // Pass dentist ID as string
            treatmentPicker.getValue(),
            datePicker.getValue(),
            timePicker.getValue(),
            attendedCheck.isSelected(),
            formStage
        ));
    
        VBox formLayout = new VBox(20, grid, saveButton);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPadding(new Insets(20));
    
        formStage.setScene(new Scene(formLayout, 600, 500));
        formStage.showAndWait();
    }

    private void showNextAvailableAppointments() {
        Map<Integer, LocalDateTime> nextAppointments = appointmentDAO.getNextAvailableAppointments();
    
        // Create a VBox to hold the list of appointments and buttons
        VBox appointmentList = new VBox(10);
        appointmentList.setPadding(new Insets(20));
        appointmentList.setAlignment(Pos.CENTER);
    
        for (Map.Entry<Integer, LocalDateTime> entry : nextAppointments.entrySet()) {
            int dentistId = entry.getKey();
            LocalDateTime availableTime = entry.getValue();
    
            // Fetch dentist details (ID, Name, Surname)
            String dentistDetails = appointmentDAO.getDentistDetails(dentistId); // Example: "1: John Doe"
    
            // Create a label for the dentist's next available appointment
            Label appointmentLabel = new Label("Dentist: " + dentistDetails + 
                ", Next Available: " + availableTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    
            // Create a "Create Appointment" button for this dentist
            Button createButton = new Button("Create Appointment");
            createButton.setOnAction(e -> {
                // Open the appointment form with pre-filled dentist and time
                showAppointmentFormWithPreFilledData(dentistId, availableTime);
            });
    
            // Add the label and button to the VBox
            HBox appointmentRow = new HBox(10, appointmentLabel, createButton);
            appointmentRow.setAlignment(Pos.CENTER);
            appointmentList.getChildren().add(appointmentRow);
        }
    
        // Show the appointments in a new dialog
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Next Available Appointments");
    
        ScrollPane scrollPane = new ScrollPane(appointmentList);
        scrollPane.setFitToWidth(true);
    
        Scene scene = new Scene(scrollPane, 600, 400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void handleUpdate() {
        Appointment selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showAppointmentForm(selected);
        } else {
            showAlert("Selection Error", "No appointment selected!", Alert.AlertType.WARNING);
        }
    }

    private void refreshTable() {
        ObservableList<Appointment> appointments = 
            FXCollections.observableArrayList(appointmentDAO.getAllAppointments());
        table.setItems(appointments);
        table.refresh();
    }

    private void showAppointmentForm(Appointment existingAppointment) {
        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.setTitle(existingAppointment == null ? "New Appointment" : "Edit Appointment");
    
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
    
        // Form components with search functionality
        ComboBox<String> patientPicker = createSearchableComboBox(appointmentDAO.getPatients());
        ComboBox<String> dentistPicker = createSearchableComboBox(appointmentDAO.getDentists());
        ComboBox<String> treatmentPicker = createSearchableComboBox(appointmentDAO.getTreatments());
    
        // Populate fields if editing
        if (existingAppointment != null) {
            patientPicker.setValue(appointmentDAO.getPatientDetails(existingAppointment.getPatientID()));
            dentistPicker.setValue(appointmentDAO.getDentistDetails(existingAppointment.getDentistID()));
            treatmentPicker.setValue(appointmentDAO.getTreatmentDetails(existingAppointment.getTreatmentID()));
        }
    
        DatePicker datePicker = new DatePicker();
        ComboBox<String> timePicker = new ComboBox<>(FXCollections.observableArrayList(getAvailableTimeSlots()));
        CheckBox attendedCheck = new CheckBox("Attended");
    
        Button createPatientButton = new Button("Create Patient");
        createPatientButton.setOnAction(e -> PatientGUI.showPatientForm(null));
    
        grid.addRow(0, new Label("Patient:"), patientPicker, createPatientButton);
        grid.addRow(1, new Label("Dentist:"), dentistPicker);
        grid.addRow(2, new Label("Treatment:"), treatmentPicker);
        grid.addRow(3, new Label("Date:"), datePicker);
        grid.addRow(4, new Label("Time:"), timePicker);
        grid.addRow(5, attendedCheck);
    
        Button saveButton = createStyledButton("Save");
        saveButton.setOnAction(e -> handleSave(
            existingAppointment,
            patientPicker.getValue(),
            dentistPicker.getValue(),
            treatmentPicker.getValue(),
            datePicker.getValue(),
            timePicker.getValue(),
            attendedCheck.isSelected(),
            formStage
        ));
    
        VBox formLayout = new VBox(20, grid, saveButton);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPadding(new Insets(20));
    
        formStage.setScene(new Scene(formLayout, 600, 500));
        formStage.showAndWait();
    }
    
    public static ComboBox<String> createSearchableComboBox(List<String> items) {
        ObservableList<String> observableItems = FXCollections.observableArrayList(items);
        ComboBox<String> comboBox = new ComboBox<>(observableItems);
        comboBox.setEditable(true);
    
        // Store the original list
        comboBox.setUserData(new ArrayList<>(items));
    
        addSearchListenerToComboBox(comboBox, items);
    
        return comboBox;
    }
    
    public static void addSearchListenerToComboBox(ComboBox<String> comboBox, List<String> originalItems) {
        comboBox.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                comboBox.setItems(FXCollections.observableArrayList(originalItems)); // Reset to full list
            } else {
                ObservableList<String> filteredItems = FXCollections.observableArrayList();
                String searchTerm = newVal.toLowerCase();
                for (String item : originalItems) {
                    if (item.toLowerCase().contains(searchTerm)) {
                        filteredItems.add(item);
                    }
                }
                comboBox.setItems(filteredItems);
            }
            comboBox.show();
        });
    }
    

    private void handleSave(Appointment existing, 
                          String patient, 
                          String dentist, 
                          String treatment,
                          LocalDate date, 
                          String time, 
                          boolean attended,
                          Stage formStage) {
        if (!validateInputs(patient, dentist, treatment, date, time)) {
            return;
        }
        try {
            int patientId = parseId(patient);
            int dentistId = parseId(dentist);
            int treatmentId = parseId(treatment);
            Date sqlDate = Date.valueOf(date);
            Time sqlTime = Time.valueOf(LocalTime.parse(time));

            if (existing == null) {
                Appointment newAppointment = new Appointment(
                    sqlDate, sqlTime, attended, treatmentId, patientId, dentistId);
                appointmentDAO.addAppointment(newAppointment);
            } else {
                existing.setDateOfAppointment(sqlDate);
                existing.setTimeOfAppointment(sqlTime);
                existing.setAttended(attended);
                appointmentDAO.updateAppointment(existing);
            }

            refreshTable();
            formStage.close();
        } catch (Exception e) {
            showAlert("Error", "Failed to save appointment: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

// In your ID parsing logic
    private int parseId(String comboValue) {
        if (comboValue == null || !comboValue.contains(":")) {
            throw new IllegalArgumentException("Invalid value: " + comboValue);
        }
        return Integer.parseInt(comboValue.split(":")[0].trim());
    }
    private boolean validateInputs(String patient, String dentist, String treatment, 
                                 LocalDate date, String time) {
        if (patient == null || dentist == null || treatment == null || 
            date == null || time == null) {
            showAlert("Validation Error", "All fields are required!", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void deleteAppointment() {
        Appointment selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            appointmentDAO.deleteAppointment(selected.getAppointmentID());
            refreshTable();
        } else {
            showAlert("Selection Error", "No appointment selected!", Alert.AlertType.WARNING);
        }
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: #3498DB; " +  
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 16px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #2980B9; " + 
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 16px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #3498DB; " + 
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 16px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        ));
        return button;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

private List<String> getAvailableTimeSlots() {
    return new ArrayList<>(Arrays.asList(
        "09:00",  "09:30",  "10:00",
         "10:30",  "11:00", 
        "11:30", "13:00",  "13:30", 
        "14:00", "14:30",  "15:00", 
        "15:30",  "16:00",  "16:30",
            "17:00",  "17:30",  "18:00"
            
    ));
}

private void backToStartScreen() {
        StartScreen startScreen = new StartScreen();
        try {
            startScreen.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

