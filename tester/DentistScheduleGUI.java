package tester;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.AppointmentCRUD;
import model.AppointmentCRUD.Appointment;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.*;
import java.time.LocalDate;
import java.util.List;
import tester.AppointmentGUI;;

public class DentistScheduleGUI extends Application {
    private TableView<Appointment> table;
    private AppointmentCRUD appointmentDAO = new AppointmentCRUD();
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Dentist Schedule");
        setupTable();
        setupMainLayout(primaryStage);
    }

    @SuppressWarnings("unchecked")
    private void setupTable() {
        table = new TableView<>();
        
        TableColumn<Appointment, String> patientColumn = new TableColumn<>("Patient");
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientFullName"));

        TableColumn<Appointment, String> dentistColumn = new TableColumn<>("Dentist");
        dentistColumn.setCellValueFactory(new PropertyValueFactory<>("dentistFullName"));

        TableColumn<Appointment, String> treatmentColumn = new TableColumn<>("Treatment");
        treatmentColumn.setCellValueFactory(new PropertyValueFactory<>("treatmentName"));
  
        TableColumn<Appointment, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfAppointment"));

        TableColumn<Appointment, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfAppointment"));

        table.getColumns().addAll(dentistColumn,  patientColumn, treatmentColumn, dateColumn, timeColumn);
    }
    
    private void setupMainLayout(Stage primaryStage) {
        DatePicker datePicker = new DatePicker();
        ComboBox<String> dentistPicker = AppointmentGUI.createSearchableComboBox(appointmentDAO.getDentistsSorting());

        Button filterbyDateButton = createStyledButton("Filter by Date");
        filterbyDateButton.setOnAction(e -> {
            LocalDate selectedDate = datePicker.getValue();
            String selectedDentist = dentistPicker.getValue();
            filterAppointmentsByDateAndDentist(selectedDate, selectedDentist);
        });
        Button filterbyDentistButton = createStyledButton("Filter by Dentist");
        filterbyDentistButton.setOnAction(e -> {
            LocalDate selectedDate = datePicker.getValue();
            String selectedDentist = dentistPicker.getValue();
            filterAppointmentsByDateAndDentist(selectedDate, selectedDentist);
        });    

        Button resetFiltersButton = createStyledButton("Reset Filters");
        resetFiltersButton.setOnAction(e -> {
            datePicker.setValue(null); 
        
            // Clear selection
            dentistPicker.getSelectionModel().clearSelection();
            dentistPicker.setValue(null);
        
            // **Destroy & Recreate Editor to Force Reset**
            Platform.runLater(() -> {
                TextField editor = dentistPicker.getEditor();
                editor.setText(""); // Clear text
                editor.setVisible(false); // Hide to force refresh
                editor.setVisible(true);  // Show again
        
                // Reset items list
                dentistPicker.setItems(FXCollections.observableArrayList(appointmentDAO.getDentists()));
        
                // Force UI update
                dentistPicker.hide();
                dentistPicker.show();
            });
        
            initialFilter(); // Reload all appointments
        });
        

        Button initialButton = createStyledButton("View All");
        initialButton.setOnAction(e -> initialFilter());
    
        Button backToStartScreen = createStyledButton("Back");
        backToStartScreen.setOnAction(e -> backToStartScreen());
    
        Label selectDateLabel = new Label("Select Date:");
        selectDateLabel.setStyle("-fx-text-fill: white;");
    
        Label selectDentistLabel = new Label("Select Dentist:");
        selectDentistLabel.setStyle("-fx-text-fill: white;");
    
        // Add dentistPicker to the layout
        HBox buttonBox = new HBox(15, selectDateLabel, datePicker, filterbyDateButton, 
                          selectDentistLabel, dentistPicker, filterbyDentistButton, 
                          initialButton, resetFiltersButton, backToStartScreen);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15));
        
        VBox layout = new VBox(15, table, buttonBox);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2C3E50; -fx-padding: 20px;");
    
        Scene scene = new Scene(layout, 1200, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    
        // Initialize table with all appointments
        List<Appointment> filteredAppointments = appointmentDAO.getAllAppointments();
        table.setItems(FXCollections.observableArrayList(filteredAppointments));
        table.refresh();
    }

    private void filterAppointmentsByDateAndDentist(LocalDate date, String dentistFullName) {
        if (date == null && (dentistFullName == null || dentistFullName.isEmpty())) {
            // If no filters are applied, show all appointments
            initialFilter();
            return;
        }
    
        List<Appointment> filteredAppointments;
    
        if (date != null && (dentistFullName == null || dentistFullName.isEmpty())) {
            // Filter only by date
            filteredAppointments = appointmentDAO.getAppointmentsByDate(date);
        } else if (dentistFullName != null && !dentistFullName.isEmpty() && date == null) {
            // Filter only by dentist full name
            filteredAppointments = appointmentDAO.getAppointmentsByDentistFullName(dentistFullName);
        } else {
            // Filter by both date and dentist full name
            filteredAppointments = appointmentDAO.getAppointmentsByDateAndDentist(date, dentistFullName);
        }
    
        table.setItems(FXCollections.observableArrayList(filteredAppointments));
        table.refresh();
    }

    @SuppressWarnings("unused")
    private Object getAppointmentsByDentistFullName(String value) {
        if (value == null) {
            showAlert("Error", "Please select a dentist.", Alert.AlertType.WARNING);
            return null;
        }
        List<Appointment> filteredAppointments = appointmentDAO.getAppointmentsByDentistFullName(value);
        table.setItems(FXCollections.observableArrayList(filteredAppointments));
        table.refresh();
        return null;
    }

    private void filterAppointmentsByDate(LocalDate date) {
        if (date == null) {
            showAlert("Error", "Please select a date.", Alert.AlertType.WARNING);
            return;
        }
        List<Appointment> filteredAppointments = appointmentDAO.getAppointmentsByDate(date);
        table.setItems(FXCollections.observableArrayList(filteredAppointments));
        table.refresh();
    }

    
    private void initialFilter() {
        List<Appointment> filteredAppointments = appointmentDAO.getAllAppointments();
        table.setItems(FXCollections.observableArrayList(filteredAppointments));
        table.refresh();
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

    private void backToStartScreen() {
        StartScreen startScreen = new StartScreen();
        try {
            startScreen.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}