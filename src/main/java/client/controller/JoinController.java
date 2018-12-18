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
            redirect("fxml/board.fxml", "Board", 920, 670, colorBox);

        } catch (NullPointerException n) {
            //n.printStackTrace();
            showAlert("Nothing has chosen", Alert.AlertType.WARNING);
        } catch (ServerConnectionException e) {
            //e.printStackTrace();
            showAlert("No connection", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void backToConnect() throws IOException {
        ServerConnector.getInstance().requestBeforeConnectToGame();
        redirect("fxml/connect.fxml", "Connect to Game", 300, 275, boardSideBox);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> sides = ClientBase.getInstance().getUnusedSides();
        List<String> colors = ClientBase.getInstance().getUnusedColors();
        if(colors != null){
            colorBox.getItems().addAll(colors);
        }
        if(sides != null){
            boardSideBox.getItems().addAll(sides);
        }


    }


}
