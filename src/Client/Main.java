package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Сетевой чат");
       // primaryStage.getIcons().add(new Image("C:\\Java\\Chat\\src\\client\\resources\\images\\stage_icon.png"));

        Parent root = FXMLLoader.load(getClass().getResource("resources/primary.fxml"));

        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
