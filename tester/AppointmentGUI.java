package tester;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import model.model.AppointmentCRUD;
import model.model.AppointmentCRUD.Appointment;
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

        TableColumn<Appointment, Integer> patientColumn = new TableColumn<>("Patient");
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));

        TableColumn<Appointment, Integer> dentistColumn = new TableColumn<>("Dentist");
        dentistColumn.setCellValueFactory(new PropertyValueFactory<>("dentistID"));

        TableColumn<Appointment, Integer> treatmentColumn = new TableColumn<>("Treatment");
        treatmentColumn.setCellValueFactory(new PropertyValueFactory<>("treatmentID"));
        
        TableColumn<Appointment, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfAppointment"));

        TableColumn<Appointment, LocalTime> timeColumn = new TableColumn<>("Time");
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

        Button backToReceptionistScreen = createStyledButton("Back");
        backToReceptionistScreen.setOnAction(e -> backToReceptionistScreen());

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, refreshButton, nextAvailableButton, backToReceptionistScreen);

        VBox layout = new VBox(15, table, buttonBox);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2C3E50; -fx-padding: 20px;");

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showNextAvailableAppointments() {
        Map<Integer, LocalDateTime> nextAppointments = appointmentDAO.getNextAvailableAppointments();
        StringBuilder message = new StringBuilder();

        for (Map.Entry<Integer, LocalDateTime> entry : nextAppointments.entrySet()) {
            message.append("Dentist ID: ").append(entry.getKey())
                .append(", Next Available: ")
                .append(entry.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append("\n");
        }

    showAlert("Next Available Appointments", message.toString(), Alert.AlertType.INFORMATION);
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

        // Form components
        ComboBox<String> patientPicker = new ComboBox<>(
            FXCollections.observableArrayList(appointmentDAO.getPatients()));
        ComboBox<String> dentistPicker = new ComboBox<>(
            FXCollections.observableArrayList(appointmentDAO.getDentists()));
        ComboBox<String> treatmentPicker = new ComboBox<>(
            FXCollections.observableArrayList(appointmentDAO.getTreatments()));
        DatePicker datePicker = new DatePicker();
        ComboBox<String> timePicker = new ComboBox<>(
            FXCollections.observableArrayList(getAvailableTimeSlots()));
        CheckBox attendedCheck = new CheckBox("Attended");

        // Populate fields if editing
        if (existingAppointment != null) {
            datePicker.setValue(existingAppointment.getDateOfAppointment().toLocalDate());
            timePicker.setValue(existingAppointment.getTimeOfAppointment().toString());
            attendedCheck.setSelected(existingAppointment.isAttended());
        }

        // Add components to grid
        grid.addRow(0, new Label("Patient:"), patientPicker);
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

        formStage.setScene(new Scene(formLayout, 400, 450));
        formStage.showAndWait();
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

    private int parseId(String comboValue) {
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
        button.setStyle("-fx-background-color: #1ABC9C; -fx-text-fill: white; " +
                       "-fx-font-size: 14px; -fx-font-weight: bold; " +
                       "-fx-padding: 10px 20px; -fx-background-radius: 5px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #16A085;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #1ABC9C;"));
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

private void backToReceptionistScreen() {
    ReceptionistScreen receptionistScreen = new ReceptionistScreen();
    receptionistScreen.start(primaryStage);
}



}