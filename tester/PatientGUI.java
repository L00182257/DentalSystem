package tester;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.model.Patient;
import model.model.PatientDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class PatientGUI extends Application {
    private TableView<Patient> table;
    private TextField searchField;
    private PatientDAO patientDAO = new PatientDAO();
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Patient Management System");

        // Table setup
        table = new TableView<>();
        setupTableColumns();
        loadPatients();

        // Search bar
        searchField = new TextField();
        searchField.setPromptText("Enter Patient ID");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchPatient());
        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER);

        // Buttons
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> showPatientForm(null));

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            Patient selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showPatientForm(selected);
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deletePatient());

        Button backToReceptionistScreen = new Button("Back");
        backToReceptionistScreen.setOnAction(e -> backToReceptionistScreen());

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton, backToReceptionistScreen);
        buttonBox.setAlignment(Pos.CENTER);

        // Layout
        VBox layout = new VBox(15, searchBox, table, buttonBox);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @SuppressWarnings("unchecked")
    private void setupTableColumns() {
        TableColumn<Patient, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));

        TableColumn<Patient, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        
        TableColumn<Patient, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        
        TableColumn<Patient, String> dobColumn = new TableColumn<>("Date of Birth");
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        
        TableColumn<Patient, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        table.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, dobColumn, emailColumn);
    }

    private void loadPatients() {
        ObservableList<Patient> patients = FXCollections.observableArrayList(patientDAO.getAllPatients());
        table.setItems(patients);
    }

    private void showPatientForm(Patient patient) {
        Stage formStage = new Stage();
        formStage.setTitle(patient == null ? "Add Patient" : "Update Patient");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        TextField dobField = new TextField();
        dobField.setPromptText("Date of Birth (YYYY-MM-DD)");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        CheckBox medCardCheckBox = new CheckBox("Medical Card");

        if (patient != null) {
            firstNameField.setText(patient.getFirstName());
            lastNameField.setText(patient.getLastName());
            dobField.setText(patient.getDOB());
            emailField.setText(patient.getEmail());
            medCardCheckBox.setSelected(patient.getMedCard());
        }

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            if (patient == null) {
                Patient newPatient = new Patient(0, firstNameField.getText(), lastNameField.getText(), dobField.getText(), emailField.getText(), null, null, null, null, medCardCheckBox.isSelected());
                patientDAO.addPatient(newPatient);
            } else {
                patient.setFirstName(firstNameField.getText());
                patient.setLastName(firstNameField.getText());
                patient.setDOB(dobField.getText());
                patient.setEmail(emailField.getText());
                patientDAO.updatePatient(patient);
            }
            loadPatients();
            formStage.close();
        });

        grid.add(new Label("First Name:"), 0, 0); grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1); grid.add(lastNameField, 1, 1);
        grid.add(new Label("Date of Birth:"), 0, 2); grid.add(dobField, 1, 2);
        grid.add(new Label("Email:"), 0, 3); grid.add(emailField, 1, 3);
        grid.add(medCardCheckBox, 1, 4);
        grid.add(saveButton, 1, 5);

        Scene formScene = new Scene(grid, 400, 300);
        formStage.setScene(formScene);
        formStage.show();
    }

    private void deletePatient() {
        Patient selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            patientDAO.deletePatient(selected.getPatientID());
            loadPatients();
        }
    }

    private void searchPatient() {
        int id = Integer.parseInt(searchField.getText());
        table.getItems().stream().filter(p -> p.getPatientID() == id).findFirst().ifPresent(p -> {
            table.getSelectionModel().select(p);
            table.scrollTo(p);
        });
    }

    private void backToReceptionistScreen() {
        ReceptionistScreen receptionistScreen = new ReceptionistScreen();
        try {
            receptionistScreen.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}