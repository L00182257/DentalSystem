package tester;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Dentist;
import model.DentistDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Validation;
import tester.PatientGUI;

public class DentistManagementGUI extends Application {
    private TableView<Dentist> table;
    private TextField searchField;
    private DentistDAO dentistDAO = new DentistDAO();
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Dentist Management System");

        // Table setup
        table = new TableView<>();
        setupTableColumns();
        loadDentists();

        // Search bar
        searchField = new TextField();
        searchField.setPromptText("Enter Dentist ID");
        Button searchButton =  createStyledButton("Search");
        searchButton.setOnAction(e -> searchDentist());
        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER);

        // Buttons
        Button addButton = createStyledButton("Add");
        addButton.setOnAction(e -> showDentistForm(null));
        
        Button updateButton =  createStyledButton("Update");
        updateButton.setOnAction(e -> {
            Dentist selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showDentistForm(selected);
        });
        
        Button deleteButton = createStyledButton("Delete");
        deleteButton.setOnAction(e -> deleteDentist());

        Button backToReceptionistScreen = createStyledButton("Back");
        backToReceptionistScreen.setOnAction(e -> backToReceptionistScreen());
        
        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton, backToReceptionistScreen);
        buttonBox.setAlignment(Pos.CENTER);

        // Layout
        VBox layout = new VBox(15, searchBox, table, buttonBox);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2C3E50; -fx-padding: 20px;");

        Scene scene = new Scene(layout, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


@SuppressWarnings("unchecked")
private void setupTableColumns() {
    TableColumn<Dentist, Integer> idColumn = new TableColumn<>("ID");
    idColumn.setCellValueFactory(new PropertyValueFactory<>("dentistID"));

    TableColumn<Dentist, String> lastNameColumn = new TableColumn<>("Last Name");
    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

    TableColumn<Dentist, String> firstNameColumn = new TableColumn<>("First Name");
    firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

    TableColumn<Dentist, String> awardingBodyColumn = new TableColumn<>("Awarding Body");
    awardingBodyColumn.setCellValueFactory(new PropertyValueFactory<>("awardingBody"));

    TableColumn<Dentist, String> specialtyColumn = new TableColumn<>("Specialty");
    specialtyColumn.setCellValueFactory(new PropertyValueFactory<>("specialty"));

    TableColumn<Dentist, String> dobColumn = new TableColumn<>("Date of Birth");
    dobColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

    TableColumn<Dentist, String> phoneColumn = new TableColumn<>("Phone Number");
    phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));

    table.getColumns().addAll(idColumn, lastNameColumn, firstNameColumn, awardingBodyColumn, specialtyColumn, dobColumn, phoneColumn);
}


    private void loadDentists() {
        ObservableList<Dentist> dentists = FXCollections.observableArrayList(dentistDAO.getAllDentists());
        table.setItems(dentists);
    }

    private void showDentistForm(Dentist dentist) {
        Stage formStage = new Stage();
        formStage.setTitle(dentist == null ? "Add Dentist" : "Update Dentist");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField awardingBodyField = new TextField();
        awardingBodyField.setPromptText("Awarding Body");
        TextField specialtyField = new TextField();
        specialtyField.setPromptText("Specialty");
        TextField dobField = new TextField();
        dobField.setPromptText("YYYY-MM-DD");
        TextField phoneNoField = new TextField();
        phoneNoField.setPromptText("Phone Number");

        // Replace the validation setup calls with these:
        PatientGUI.setupValidationStyling(firstNameField, Validation::validateName);
        PatientGUI.setupValidationStyling(lastNameField, Validation::validateName);
        PatientGUI.setupValidationStyling(dobField, Validation::validateDateOfBirth);
        PatientGUI.setupValidationStyling(phoneNoField, Validation::validatePhone);
    

        if (dentist != null) {
            lastNameField.setText(dentist.getLastName());
            firstNameField.setText(dentist.getFirstName());
            awardingBodyField.setText(dentist.getAwardingBody());
            specialtyField.setText(dentist.getSpecialty());
            dobField.setText(dentist.getDateOfBirth());
            phoneNoField.setText(String.valueOf(dentist.getPhoneNo()));
        }

        Button saveButton = createStyledButton("Save");
        saveButton.setOnAction(e -> {
            if (dentist == null) {
                Dentist newDentist = new Dentist(0, lastNameField.getText(), firstNameField.getText(), awardingBodyField.getText(), specialtyField.getText(), dobField.getText(), Integer.parseInt(phoneNoField.getText()));
                dentistDAO.addDentist(newDentist);
            } else {
                dentist.setLastName(lastNameField.getText());
                dentist.setFirstName(firstNameField.getText());
                dentist.setAwardingBody(awardingBodyField.getText());
                dentist.setSpecialty(specialtyField.getText());
                dentist.setDateOfBirth(dobField.getText());
                dentist.setPhoneNo(Integer.parseInt(phoneNoField.getText()));
                dentistDAO.updateDentist(dentist);
            }
            loadDentists();
            formStage.close();
        });

        grid.add(new Label("Last Name:"), 0, 0); grid.add(lastNameField, 1, 0);
        grid.add(new Label("First Name:"), 0, 1); grid.add(firstNameField, 1, 1);
        grid.add(new Label("Awarding Body:"), 0, 2); grid.add(awardingBodyField, 1, 2);
        grid.add(new Label("Specialty:"), 0, 3); grid.add(specialtyField, 1, 3);
        grid.add(new Label("Date of Birth:"), 0, 4); grid.add(dobField, 1, 4);
        grid.add(new Label("Phone Number:"), 0, 5); grid.add(phoneNoField, 1, 5);
        grid.add(saveButton, 1, 6);

        Scene formScene = new Scene(grid, 400, 300);
        formStage.setScene(formScene);
        formStage.show();
    }

    private void deleteDentist() {
        Dentist selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            dentistDAO.deleteDentist(selected.getDentistID());
            loadDentists();
        }
    }

    
    private void searchDentist() {
        String searchText = searchField.getText().trim().toLowerCase();
        
        if (searchText.isEmpty()) {
            table.getSelectionModel().clearSelection();
            return;
        }
    
        for (Dentist d : table.getItems()) {
            if (String.valueOf(d.getDentistID()).equals(searchText) || 
                d.getFirstName().toLowerCase().contains(searchText) || 
                d.getLastName().toLowerCase().contains(searchText)) {
                
                table.getSelectionModel().select(d);
                table.scrollTo(d);
                return; // Stop after selecting the first match
            }
        }
    }


    private void backToReceptionistScreen() {
        ReceptionistScreen receptionistScreen = new ReceptionistScreen();
        try {
            receptionistScreen.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
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
                        "-fx-cursor: hand;");
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #2980B9; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10px 16px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: #3498DB; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10px 16px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand;"));
        return button;
    }

}
