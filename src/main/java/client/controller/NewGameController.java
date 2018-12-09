package client.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;


public class NewGameController implements Initializable {

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
    public void setBoard(){
        boardType = boardTypeBox.getValue();
    }
    @FXML
    public void setMovement(){
        movementType = movementTypeBox.getValue();
    }
    @FXML
    public void setNoPlayers(){
        numberOfPlayers = numberOfPlayersBox.getValue();
        System.out.println(numberOfPlayers);
    }
    @FXML
    public void setNoPawns(){
        numberOfPawns = numberOfPawnsBox.getValue();
    }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        numberOfPlayersBox.getItems().addAll(2, 3, 4, 6);
    }

}
