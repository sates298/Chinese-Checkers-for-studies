package client.controller;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;


import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import java.util.ResourceBundle;

public class ConnectController extends AbstractController implements Initializable {

    private Integer gameId;

    @FXML
    private ComboBox<Integer> gameIdBox;

    @FXML
    public void connectAction() throws IOException {
        try {
            gameId = gameIdBox.getValue();
        }catch(Exception e){
            showAlert("empty comboBox", Alert.AlertType.WARNING);
        }
        redirect("fxml/join.fxml", "Join to Game", 300, 275, gameIdBox);
    }

    @FXML
    public void backToMenu() throws IOException{
        redirect("fxml/menu.fxml", "Trylma The Game", 300, 275, gameIdBox);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Integer> openGames = new ArrayList<>();
        openGames.add(1);
        openGames.add(2);
        //todo get List<Integer> gamesId's from serwer
        gameIdBox.getItems().addAll(openGames);
    }
}
