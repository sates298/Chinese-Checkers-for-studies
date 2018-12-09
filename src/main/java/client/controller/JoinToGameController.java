package client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class JoinToGameController {

    @FXML
    private Button joinButton;

    @FXML
    private TextField gameId;


    @FXML
    public void connectAction(){
        joinButton.setVisible(true);
    }


}
