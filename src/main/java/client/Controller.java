package client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Button exitButton;

    @FXML
    public void exit(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
