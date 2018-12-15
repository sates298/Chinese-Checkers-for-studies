package client.controller;


import client.ClientBase;

import client.network.ServerConnector;
import client.drawableBoard.DrawableField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.Pane;


import java.net.URL;
import java.util.ResourceBundle;

public class BoardController extends AbstractController implements Initializable {

    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;
    @FXML
    private Label label5;
    @FXML
    private Label label6;

    @FXML
    private Button exitButton;

    @FXML
    private Pane pane;

    @FXML
    public void exitAction(){
        exit(exitButton);
    }

    @FXML
    public void endTurn(){
    }

    public void drawBoard(DrawableField[][] board){
        ClientBase.getInstance().getBoardDraw().drawBoard(this.pane, board);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServerConnector.getInstance().setBoardController(this);
        drawBoard(ClientBase.getInstance().getStartedBoard());
    }
}
