package client.controller;


import client.ClientBase;

import client.network.InGameActionsHandler;
import client.network.ServerConnectionException;
import client.network.ServerConnector;
import client.drawableBoard.DrawableField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


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
            startButton.setVisible(false);
            for(int i=0; i<ClientBase.getInstance().getPlayersToLabel().size(); i++){
                makeLabel(
                        i,
                        ClientBase.getInstance().getPlayersToLabel().get(i)
                );
            }
            ServerConnector.getInstance().requestStartGame();

        } catch (ServerConnectionException e) {
            startButton.setVisible(true);
            showAlert("Connection Error", Alert.AlertType.ERROR);
        }
    }

    private void makeLabel(int id, Paint color){
        switch(id){
            case 1:
                label1.setText("PLAYER 1");
                label1.setTextFill(color);
                break;
            case 2:
                label2.setText("PLAYER 2");
                label2.setTextFill(color);
                break;
            case 3:
                label3.setText("PLAYER 3");
                label3.setTextFill(color);
                break;
            case 4:
                label4.setText("PLAYER 4");
                label4.setTextFill(color);
                break;
            case 5:
                label5.setText("PLAYER 5");
                label5.setTextFill(color);
                break;
            case 6:
                label6.setText("PLAYER 6");
                label6.setTextFill(color);
                break;
            default:
                break;
        }
    }

    private void paneClicked(DrawableField field) {
        if (ClientBase.getInstance().getFirstClicked() != null && field.getType().equals("\"EmptyField\"")) {
            ClientBase.getInstance().setLastClicked(field);
            try {
                handler.requestMove(
                        ClientBase.getInstance().getPlayerId(),
                        ClientBase.getInstance().getFirstClicked().getX(),
                        ClientBase.getInstance().getFirstClicked().getY(),
                        ClientBase.getInstance().getLastClicked().getX(),
                        ClientBase.getInstance().getLastClicked().getY()
                );
                ClientBase.getInstance().getFirstClicked().setFill(
                        ClientBase.getInstance().getFirstClicked().getMainColor()
                );
            } catch (ServerConnectionException e) {
                showAlert("Wrong move", Alert.AlertType.INFORMATION);
            }


        } else if (field.getType().equals("\"Pawn\"")) {
            if (ClientBase.getInstance().getFirstClicked() != null) {
                ClientBase.getInstance().getFirstClicked().setFill(
                        ClientBase.getInstance().getFirstClicked().getMainColor()
                );
            }
            ClientBase.getInstance().setFirstClicked(field);
            field.setFill(Color.MEDIUMAQUAMARINE);
        }
    }


    @FXML
    public void endTurn() {
        try {

            ClientBase.getInstance().setFirstClicked(null);
            ClientBase.getInstance().setLastClicked(null);

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
