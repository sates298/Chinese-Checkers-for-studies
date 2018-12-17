package client;

import client.network.ServerConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // connect to server
        ServerConnector.getInstance().makeConnection("localhost", 1235);
        Parent root =
                FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/menu.fxml")));
        primaryStage.setTitle("Trylma The Game");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
