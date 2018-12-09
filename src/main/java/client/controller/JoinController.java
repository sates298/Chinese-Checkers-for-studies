package client.controller;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;


import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

public class JoinController extends AbstractController implements Initializable {

    private String boardSide;
    private String color;

    @FXML
    private ComboBox<String> boardSideBox;
    @FXML
    private ComboBox<String> colorBox;

    @FXML
    public void joinToBoard() throws IOException {
        try {
            boardSide = boardSideBox.getValue();
            color = colorBox.getValue();
        }catch(Exception e){
            showAlert("empty combo boxes", Alert.AlertType.WARNING);
        }
        redirect("fxml/board.fxml", "Board", 920, 670, colorBox);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //todo fill both comboBoxes
        boardSideBox.getItems().addAll();
        colorBox.getItems().addAll();
    }



}
