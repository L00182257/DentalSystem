package tester;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Patient;
import model.PatientDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class PatientGUI extends Application {
    private static TableView<Patient> table;
    private TextField searchField;
    private static PatientDAO patientDAO = new PatientDAO();
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
            searchField.setPromptText("Enter Patient Name and D.O.B...");
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
            dobColumn.setCellValueFactory(new PropertyValueFactory<>("DOB"));
            
            TableColumn<Patient, String> emailColumn = new TableColumn<>("Email");
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    
            TableColumn<Patient, String> streetColumn = new TableColumn<>("Street");
            streetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));
    
            TableColumn<Patient, String> townColumn = new TableColumn<>("Town");
            townColumn.setCellValueFactory(new PropertyValueFactory<>("town"));
    
            TableColumn<Patient, String> countyColumn = new TableColumn<>("County");
            countyColumn.setCellValueFactory(new PropertyValueFactory<>("county"));
    
            TableColumn<Patient, String> eircodeColumn = new TableColumn<>("Eircode");
            eircodeColumn.setCellValueFactory(new PropertyValueFactory<>("eircode"));
    
            TableColumn<Patient, Boolean> medCardColumn = new TableColumn<>("Medcard");
            medCardColumn.setCellValueFactory(new PropertyValueFactory<>("medCard"));
    
            TableColumn<Patient, Double> amtOwedColumn = new TableColumn<>("Amount Owed");
            amtOwedColumn.setCellValueFactory(new PropertyValueFactory<>("amtOwed"));
            
            table.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, dobColumn, emailColumn, streetColumn, townColumn, eircodeColumn, medCardColumn, amtOwedColumn);
        }
    
        private static void loadPatients() {
                ObservableList<Patient> patients = FXCollections.observableArrayList(patientDAO.getAllPatients());
            table.setItems(patients);
        }
    
        static void showPatientForm(Patient patient) {
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
            dobField.setPromptText("YYYY-MM-DD");
            TextField emailField = new TextField();
            emailField.setPromptText("Email");
            CheckBox medCardCheckBox = new CheckBox("Medical Card");
            TextField streetField = new TextField();
            streetField.setPromptText("Street");
            TextField townField = new TextField();
            townField.setPromptText("Town");
            TextField countyField = new TextField();
            countyField.setPromptText("County");
            TextField eircodeField = new TextField();
            eircodeField.setPromptText("Eircode");
            TextField amtOwedField = new TextField();
            amtOwedField.setPromptText("Amount Owed");
    
            if (patient != null) {
                firstNameField.setText(patient.getFirstName());
                lastNameField.setText(patient.getLastName());
                dobField.setText(patient.getDOB());
                emailField.setText(patient.getEmail());
                medCardCheckBox.setSelected(patient.getMedCard());
                streetField.setText(patient.getStreet());
                townField.setText(patient.getTown());
                countyField.setText(patient.getCounty());
                eircodeField.setText(patient.getEircode());
                amtOwedField.setText(String.valueOf(patient.getAmtOwed()));
            }
    
            Button saveButton = new Button("Save");
            saveButton.setOnAction(e -> {
                if (patient == null) {

                    Double.parseDouble(amtOwedField.getText());
                    Patient newPatient = new Patient(0, firstNameField.getText(), lastNameField.getText(), dobField.getText(), emailField.getText(), streetField.getText(), townField.getText(), countyField.getText(), eircodeField.getText(), medCardCheckBox.isSelected(), Double.parseDouble(amtOwedField.getText()));
                    patientDAO.addPatient(newPatient);
                } else {
                    patient.setFirstName(firstNameField.getText());
                    patient.setLastName(lastNameField.getText());
                    patient.setDOB(dobField.getText());
                    patient.setEmail(emailField.getText());
                    patient.setStreet(streetField.getText());
                    patient.setTown(townField.getText());
                    patient.setCounty(countyField.getText());
                    patient.setEircode(eircodeField.getText());
                    patient.setMedCard(medCardCheckBox.isSelected());
                    patient.setAmtOwed(Double.parseDouble(amtOwedField.getText()));
    
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
        grid.add(new Label("Street:"), 0, 5); grid.add(streetField, 1, 5);
        grid.add(new Label("Town:"), 0, 6); grid.add(townField, 1, 6);
        grid.add(new Label("County:"), 0, 7); grid.add(countyField, 1, 7);
        grid.add(new Label("Eircode:"), 0, 8); grid.add(eircodeField, 1, 8);
        grid.add(new Label("Amount Owed:"), 0, 9); grid.add(amtOwedField, 1, 9);
        grid.add(saveButton, 1, 10);

        Scene formScene = new Scene(grid, 400, 450);
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
        String input = searchField.getText().toLowerCase().trim();
    
        String[] searchParams = input.split(" "); 
    
        if (searchParams.length == 3) {
            String firstName = searchParams[0].toLowerCase();
            String lastName = searchParams[1].toLowerCase();
            String dob = searchParams[2].toLowerCase();
    
            ObservableList<Patient> filteredPatients = FXCollections.observableArrayList();
    
            for (Patient p : patientDAO.getAllPatients()) {
                if (p.getFirstName().toLowerCase().contains(firstName) &&
                    p.getLastName().toLowerCase().contains(lastName) &&
                    p.getDOB().toLowerCase().contains(dob)) {
                    filteredPatients.add(p);
                }
            }
    
            table.setItems(filteredPatients);
    
            if (filteredPatients.isEmpty()) {
                showAlert("No Results Found", "No patients found with that name and date of birth.");
            }
    
        } else {
            showAlert("Invalid Input", "Please enter First Name, Last Name, and Date of Birth (YYYY-MM-DD).");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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