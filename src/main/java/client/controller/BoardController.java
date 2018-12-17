package client.controller;


import client.ClientBase;

import client.drawableBoard.SixPointedStarDraw;
import client.network.InGameActionsHandler;
import client.network.ServerConnectionException;
import client.network.ServerConnector;
import client.drawableBoard.DrawableField;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


import java.net.URL;
import java.util.ResourceBundle;

public class BoardController extends AbstractController implements Initializable {

    private InGameActionsHandler handler;


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
    private Button startButton;

    @FXML
    private Pane pane;

    @FXML
    public void exitAction() {
        exit(exitButton);
    }

    @FXML
    public void startAction() {
        try {
            ServerConnector.getInstance().requestStartGame();

        } catch (ServerConnectionException e) {
            showAlert("Connection Error", Alert.AlertType.ERROR);
        }
    }

    private void paneClicked(DrawableField field) {
        if (ClientBase.getInstance().getFirstClicked() != null && field.getType().equals("\"EmptyField\"")) {
            ClientBase.getInstance().setLastClicked(field);
            /*try {
                handler.requestMove(
                        ClientBase.getInstance().getPlayerId(),
                        ClientBase.getInstance().getFirstClicked().getX(),
                        ClientBase.getInstance().getFirstClicked().getY(),
                        ClientBase.getInstance().getLastClicked().getX(),
                        ClientBase.getInstance().getLastClicked().getY()
                );
            } catch (ServerConnectionException e) {
                showAlert("Wrong move", Alert.AlertType.INFORMATION);
            }*/

            ClientBase.getInstance().getFirstClicked().setFill(
                    ClientBase.getInstance().getFirstClicked().getMainColor()
            );
        } else if (field.getType().equals("\"Pawn\"")) {
            if(ClientBase.getInstance().getFirstClicked() != null){
                ClientBase.getInstance().getFirstClicked().setFill(
                        ClientBase.getInstance().getFirstClicked().getMainColor()
                );
            }
            ClientBase.getInstance().setFirstClicked(field);
            field.setFill(Color.CHOCOLATE);
        }
    }




    @FXML
    public void endTurn() {
        try {

            ClientBase.getInstance().setFirstClicked(null);
            ClientBase.getInstance().setLastClicked(null);

            //todo somewhere we have to set playerId for client
            handler.requestEndTurn(
                    ClientBase.getInstance().getPlayerId()
            );
        } catch (ServerConnectionException e) {
            showAlert("Connection Error", Alert.AlertType.ERROR);
        }
    }

    public void drawBoard(DrawableField[][] board) {
        ClientBase.getInstance().getBoardDraw().drawBoard(this.pane, board);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServerConnector.getInstance().setBoardController(this);

        drawBoard(ClientBase.getInstance().getStartedBoard());
        this.handler = new InGameActionsHandler();

        for (Node n : pane.getChildren()) {
            if (n instanceof DrawableField) {
                n.setOnMouseClicked(event -> paneClicked((DrawableField) n));
            }
        }


    }
}
