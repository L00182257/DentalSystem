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

public class DentistScreen extends Application {
    private VBox layout;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Dentist Dashboard");

        // Title
        Text title = new Text("Dentist Dashboard");
        title.setFont(Font.font("Arial", 28));
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-weight: bold; ");

        Button scheduleButton = createStyledButton("Schedule");
        scheduleButton.setOnAction(e -> openScheduleScreen());

        Button backToStartScreen = createStyledButton("Back");
        backToStartScreen.setOnAction(e -> backToStartScreen(primaryStage));

        
        this.layout =  new VBox(20, title,  scheduleButton, backToStartScreen);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2C3E50; -fx-padding: 50;");

        scheduleButton.setMaxWidth(Double.MAX_VALUE);

        Scene scene = new Scene(layout, 450, 450); 
        primaryStage.setScene(scene);
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


    private void openScheduleScreen() {
        DentistScheduleGUI scheduleGUI = new DentistScheduleGUI();
        scheduleGUI.start(primaryStage);  
    }

    private void backToStartScreen(Stage primaryStage) {
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
