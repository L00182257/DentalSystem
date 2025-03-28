package tester;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.TreatmentCRUD;
import model.TreatmentCRUD.Treatment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class TreatmentGUI extends Application {
    private TableView<Treatment> table;
    private TextField searchField;
    private TreatmentCRUD treatmentCRUD = new TreatmentCRUD();
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Treatment Management System");

        // Table setup
        table = new TableView<>();
        setupTableColumns();
        loadTreatments();

        // Search bar
        searchField = new TextField();
        searchField.setPromptText("Enter Treatment ID");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchTreatment());
        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER);

        // Buttons
        Button addButton = new Button("Add Treatment");
        addButton.setOnAction(e -> showAddTreatmentDialog());

        Button updateButton = new Button("Update Treatment");
        updateButton.setOnAction(e -> {
            Treatment selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showUpdateTreatmentDialog(selected);
        });

        Button deleteButton = new Button("Delete Treatment");
        deleteButton.setOnAction(e -> deleteTreatment());

        Button backToReceptionistScreen = new Button("Back");
        backToReceptionistScreen.setOnAction(e -> backToReceptionistScreen());

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton, backToReceptionistScreen);
        buttonBox.setAlignment(Pos.CENTER);

        // Layout
        VBox layout = new VBox(15, searchBox, table, buttonBox);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @SuppressWarnings("unchecked")
    private void setupTableColumns() {
        TableColumn<Treatment, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("treatmentID"));

        TableColumn<Treatment, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("treatmentType"));

        TableColumn<Treatment, Integer> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Treatment, Integer> lengthColumn = new TableColumn<>("Length (mins)");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        table.getColumns().addAll(idColumn, typeColumn, priceColumn, lengthColumn);
    }

    private void loadTreatments() {
        List<Treatment> treatments = treatmentCRUD.getAllTreatments();
        ObservableList<Treatment> treatmentList = FXCollections.observableArrayList(treatments);
        table.setItems(treatmentList);
    }

    private void showAddTreatmentDialog() {
        Dialog<Treatment> dialog = new Dialog<>();
        dialog.setTitle("Add Treatment");
    
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
    
        TextField typeField = new TextField();
        TextField priceField = new TextField();
        TextField lengthField = new TextField();
    
        grid.add(new Label("Type:"), 0, 0);
        grid.add(typeField, 1, 0);
        grid.add(new Label("Price:"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Length (mins):"), 0, 2);
        grid.add(lengthField, 1, 2);
    
        dialog.getDialogPane().setContent(grid);
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
    
        dialog.setResultConverter(button -> {
            if (button == addButtonType) {
                try {
                    String type = typeField.getText();
                    int price = Integer.parseInt(priceField.getText());
                    int length = Integer.parseInt(lengthField.getText());
                    Treatment treatment = new Treatment(0, type, price, length);
                    treatmentCRUD.addTreatment(treatment);
                    loadTreatments(); // Refresh the table
                } catch (NumberFormatException e) {
                    showAlert("Invalid input. Please enter valid numbers.");
                }
            }
            return null;
        });
    
        dialog.showAndWait();
    }

    private void showUpdateTreatmentDialog(Treatment treatment) {
      Dialog<Treatment> dialog = new Dialog<>();
      dialog.setTitle("Update Treatment");

      GridPane grid = new GridPane();
      grid.setPadding(new Insets(20));
      grid.setHgap(10);
      grid.setVgap(10);

      TextField typeField = new TextField(treatment.getTreatmentType());
      TextField priceField = new TextField(String.valueOf(treatment.getPrice()));
      TextField lengthField = new TextField(String.valueOf(treatment.getLength()));

      grid.add(new Label("Type:"), 0, 0);
      grid.add(typeField, 1, 0);
      grid.add(new Label("Price:"), 0, 1);
      grid.add(priceField, 1, 1);
      grid.add(new Label("Length (mins):"), 0, 2);
      grid.add(lengthField, 1, 2);

      dialog.getDialogPane().setContent(grid);
      ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

      dialog.setResultConverter(button -> {
          if (button == updateButtonType) {
              try {
                  treatment.setTreatmentType(typeField.getText());
                  treatment.setPrice(Integer.parseInt(priceField.getText()));
                  treatment.setLength(Integer.parseInt(lengthField.getText()));
                  treatmentCRUD.updateTreatment(treatment); // Assuming you have an update method
                  loadTreatments(); // Refresh the table
              } catch (NumberFormatException e) {
                  showAlert("Invalid input. Please enter valid numbers.");
              }
          }
          return null;
      });

      dialog.showAndWait();
    }


    private void deleteTreatment() {
        Treatment selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            treatmentCRUD.deleteTreatment(selected.getTreatmentID());
            loadTreatments();
        }
    }

    private void searchTreatment() {
        try {
            int id = Integer.parseInt(searchField.getText());
            table.getItems().stream().filter(t -> t.getTreatmentID() == id).findFirst().ifPresent(t -> {
                table.getSelectionModel().select(t);
                table.scrollTo(t);
            });
        } catch (NumberFormatException e) {
            showAlert("Invalid ID. Please enter a valid number.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
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