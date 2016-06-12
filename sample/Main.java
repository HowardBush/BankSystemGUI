package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("System bankowy");
        primaryStage.setScene(new Scene(root, 300*2, 275*1.5));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
