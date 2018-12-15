package client.controller;

import client.ClientBase;
import client.network.ServerConnectionException;
import client.network.ServerConnector;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;


import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;


public class NewGameController extends AbstractController implements Initializable {

    private String boardType;
    private String movementType;
    private int numberOfPlayers;
    private int numberOfPawns;


    @FXML
    private ComboBox<String> boardTypeBox;
    @FXML
    private ComboBox<String> movementTypeBox;
    @FXML
    private ComboBox<Integer> numberOfPlayersBox;
    @FXML
    private ComboBox<Integer> numberOfPawnsBox;

    @FXML
    public void create() throws IOException{
        try {
            ClientBase.getInstance().setBoardType(boardTypeBox.getValue());
            ClientBase.getInstance().setMovementType(movementTypeBox.getValue());
            numberOfPlayers = numberOfPlayersBox.getValue();
            numberOfPawns = numberOfPawnsBox.getValue();

            ServerConnector.getInstance().requestCreateGame(numberOfPlayers, numberOfPawns);
            redirect("fxml/join.fxml", "Join to Game", 300, 275, boardTypeBox);

        }catch (NullPointerException e){
            showAlert("empty combo boxes", Alert.AlertType.WARNING);
        } catch (ServerConnectionException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void backToMenu() throws IOException {
        redirect("fxml/menu.fxml", "Trylma The Game", 300, 275, boardTypeBox);
    }


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        boardTypeBox.getItems().addAll("SixPointedStar");
        movementTypeBox.getItems().addAll("main");
        numberOfPlayersBox.getItems().addAll(2, 3, 4, 6);
        numberOfPawnsBox.getItems().addAll(10);

    }

}
