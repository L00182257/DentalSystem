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

public class StartScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dental Booking System");

        Text title = new Text("DENTAL BOOKING SYSTEM");
        title.setFont(Font.font("Arial", 28));
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-weight: bold; ");

        Button receptionistButton = createStyledButton("Receptionist");
        receptionistButton.setOnAction(e -> openReceptionistScreen(primaryStage));

        Button dentistButton = createStyledButton("Dentist");
        dentistButton.setOnAction(e -> openDentistScreen(primaryStage));
      
        VBox layout = new VBox(20, title, receptionistButton, dentistButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2C3E50; -fx-padding: 50;");

        receptionistButton.setPrefWidth(300);
        dentistButton.setPrefWidth(300);
        
        Scene scene = new Scene(layout, 450, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
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

   
   private void openReceptionistScreen(Stage primaryStage) {
        ReceptionistScreen receptionistScreen = new ReceptionistScreen();
        receptionistScreen.start(primaryStage);
    }

    private void openDentistScreen(Stage primaryStage) {
        DentistScreen dentistScreen = new DentistScreen();
        dentistScreen.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

