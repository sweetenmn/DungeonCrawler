package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = FXMLLoader.load(getClass().getResource("gui.fxml"));
        primaryStage.setTitle("GenericRPG");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);
    }
    public static void main(String[] args){
        launch(args);
    }
}
