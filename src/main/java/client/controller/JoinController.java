package client.controller;

import client.network.ServerConnectionException;
import client.network.ServerConnector;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;


import java.io.IOException;
import java.net.URL;

import java.util.*;

public class JoinController extends AbstractController implements Initializable {

    @FXML
    private ComboBox<String> boardSideBox;
    @FXML
    private ComboBox<String> colorBox;

    @FXML
    public void joinToBoard() throws IOException {
        try {
            String boardSide = boardSideBox.getValue();
            String color = colorBox.getValue();
            if(color == null || boardSide == null){
                throw new NullPointerException();
            }
            ServerConnector.getInstance().requestJoinGame(boardSide, color);
            //ServerConnector.getInstance().requestStartGame();
            redirect("fxml/board.fxml", "Board", 920, 670, colorBox);

        } catch (NullPointerException n) {
            showAlert("Nothing has chosen", Alert.AlertType.WARNING);
        } catch (ServerConnectionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardSideBox.getItems().addAll("TOP", "LEFT_TOP", "RIGHT_TOP", "BOTTOM", "LEFT_BOTTOM", "RIGHT_BOTTOM");
        colorBox.getItems().addAll("RED", "GREEN", "BLACK",  "BLUE" , "YELLOW" , "PURPLE");
    }


}
