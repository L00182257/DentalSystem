package tester;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ReceptionistScreen extends Application {

    private VBox layout;
    private Scene mainScene;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Receptionist Dashboard");

        // Title
        Text title = new Text("Receptionist Dashboard");
        title.setFont(Font.font("Arial", 28));
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-weight: bold;");

        Button appointmentButton = createStyledButton("Appointment");
        appointmentButton.setOnAction(e -> openAppointmentScreen());

        Button scheduleButton = createStyledButton("Schedule");
        scheduleButton.setOnAction(e -> openScheduleScreen());

        Button treatmentListButton = createStyledButton("Treatment List");
        treatmentListButton.setOnAction(e -> openTreatmentListScreen());

        Button patientListButton = createStyledButton("Patient List");
        patientListButton.setOnAction(e -> openPatientListScreen());

        Button dentistListButton = createStyledButton("Dentist List");
        dentistListButton.setOnAction(e -> openDentistListScreen());

        Button backToStartScreen = createStyledButton("Back");
        backToStartScreen.setOnAction(e -> backToStartScreen());

        layout = new VBox(20, title, appointmentButton, scheduleButton, treatmentListButton, patientListButton, dentistListButton, backToStartScreen);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2C3E50; -fx-padding: 50;");

        appointmentButton.setMaxWidth(Double.MAX_VALUE);
        scheduleButton.setMaxWidth(Double.MAX_VALUE);
        treatmentListButton.setMaxWidth(Double.MAX_VALUE);
        patientListButton.setMaxWidth(Double.MAX_VALUE);
        dentistListButton.setMaxWidth(Double.MAX_VALUE);

        mainScene = new Scene(layout, 450, 450);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    // Button Styling
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: #3498DB; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        );
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #2980B9; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #3498DB; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 8px; " +
            "-fx-cursor: hand;"
        ));
        return button;
    }

    private void openAppointmentScreen() {
        AppointmentGUI appointmentGUI = new AppointmentGUI();
        layout.getChildren().clear();
        appointmentGUI.start(primaryStage);
    }

    private void openScheduleScreen() {
        DentistScheduleGUI scheduleGUI = new DentistScheduleGUI();
        layout.getChildren().clear();
        scheduleGUI.start(primaryStage);
    }

    private void openTreatmentListScreen() {
        TreatmentGUI treatmentGUI = new TreatmentGUI();
        layout.getChildren().clear();
        treatmentGUI.start(primaryStage);
    }

    private void openPatientListScreen() {
        PatientGUI patientGUI = new PatientGUI();
        layout.getChildren().clear();
        patientGUI.start(primaryStage);
    }

    private void openDentistListScreen() {
        DentistManagementGUI dentistGUI = new DentistManagementGUI();
        layout.getChildren().clear();
        dentistGUI.start(primaryStage);
    }

    private void backToStartScreen() {
        StartScreen startScreen = new StartScreen();
        try {
            startScreen.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}