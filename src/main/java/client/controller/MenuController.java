package client.controller;

import client.network.ServerConnectionException;
import client.network.ServerConnector;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;


import java.io.IOException;


public class MenuController extends AbstractController {

    @FXML
    private Button exitButton;

    @FXML
    public void joinToGame() throws IOException {
        try {
            ServerConnector.getInstance().requestBeforeConnectToGame();
            redirect("fxml/connect.fxml", "Connect to Game", 300, 275, exitButton);
        } catch (ServerConnectionException e) {
            showAlert("Don't connect", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void newGame() throws IOException {
        redirect("fxml/newGame.fxml", "Create New Game", 350, 450, exitButton);
    }

    @FXML
    public void exitAction() {
        exit(exitButton);
    }
}
