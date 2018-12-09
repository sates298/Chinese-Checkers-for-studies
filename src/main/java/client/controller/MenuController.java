package client.controller;

import javafx.fxml.FXML;

import javafx.scene.control.Button;


import java.io.IOException;


public class MenuController extends AbstractController {

    @FXML
    private Button exitButton;

    @FXML
    public void joinToGame() throws IOException {
        redirect("fxml/connect.fxml", "Connect to Game", 300, 275, exitButton);
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
