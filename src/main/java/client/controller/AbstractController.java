package client.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public abstract class AbstractController {

    public void redirect(String direction, String title, double width, double height, Node node) throws IOException {
        Parent root =
                FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(direction)));
        Stage chooseStage = new Stage();
        chooseStage.setTitle(title);
        chooseStage.setScene(new Scene(root, width, height));
        chooseStage.show();

        exit(node);
    }

    public void exit(Node node){
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void showAlert(String message,  Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(type.toString());
        alert.setHeaderText(type.toString());
        alert.setContentText(message);
        alert.showAndWait();
    }
}
