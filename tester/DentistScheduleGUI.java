package tester;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.model.AppointmentCRUD;
import model.model.AppointmentCRUD.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.*;
import java.time.LocalDate;
import java.util.List;

public class DentistScheduleGUI extends Application {
    private TableView<Appointment> table;
    private AppointmentCRUD appointmentDAO = new AppointmentCRUD();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dentist Schedule");
        setupTable();
        setupMainLayout(primaryStage);
    }

    @SuppressWarnings("unchecked")
    private void setupTable() {
        table = new TableView<>();
        
        TableColumn<Appointment, Integer> dentistColumn = new TableColumn<>("Dentist ID");
        dentistColumn.setCellValueFactory(new PropertyValueFactory<>("dentistID"));

        TableColumn<Appointment, String> dentistNameColumn = new TableColumn<>("Dentist Name");
        dentistNameColumn.setCellValueFactory(new PropertyValueFactory<>("dentistName"));

        TableColumn<Appointment, Integer> patientColumn = new TableColumn<>("Patient ID");
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));

        TableColumn<Appointment, Integer> treatmentColumn = new TableColumn<>("Treatment ID");
        treatmentColumn.setCellValueFactory(new PropertyValueFactory<>("treatmentID"));
        
        TableColumn<Appointment, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfAppointment"));

        TableColumn<Appointment, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfAppointment"));

        table.getColumns().addAll(dentistColumn, dentistNameColumn, patientColumn, treatmentColumn, dateColumn, timeColumn);
    }
    
    private void setupMainLayout(Stage primaryStage) {
        DatePicker datePicker = new DatePicker(LocalDate.now());
        Button filterButton = createStyledButton("Filter by Date");
        filterButton.setOnAction(e -> filterAppointmentsByDate(datePicker.getValue()));

        Button initialButton = createStyledButton("View All");
        initialButton.setOnAction(e -> initialFilter());

        Label selectDateLabel = new Label("Select Date:");
selectDateLabel.setStyle("-fx-text-fill: white;");
        HBox buttonBox = new HBox(15, selectDateLabel, datePicker, filterButton, initialButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15));
        
        VBox layout = new VBox(15, table, buttonBox);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2C3E50; -fx-padding: 20px;");

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // filterAppointmentsByDate(LocalDate.now());
        List<Appointment> filteredAppointments = appointmentDAO.getAllAppointments();
        table.setItems(FXCollections.observableArrayList(filteredAppointments));
        table.refresh();
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
}
