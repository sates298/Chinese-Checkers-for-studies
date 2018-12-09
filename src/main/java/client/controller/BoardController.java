package client.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import server.board.Board;
import server.board.SixPointedStar;
import server.creator.boardCreator.SixPointedStarCreator;
import server.creator.fieldCreator.FieldCreator;
import server.creator.fieldCreator.PawnCreator;
import server.field.EmptyField;
import server.field.NoField;
import server.field.Pawn;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardController implements Initializable {

    private Circle[][] board;
    private final double radius = 15;
    private final double spacing = 3;
    private Paint backgroundColor = Color.WHITE;

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
    public void exit(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void endTurn(){
    }

    @FXML
    public void drawBoard(){
        pane.getChildren().clear();
        //todo this part is only to check correct drawing
        SixPointedStar star = (SixPointedStar) (new SixPointedStarCreator()).createBoard();
        setPawns(star);
        /////////
        convertTableToTable(star);
        pane.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));

        double layoutX , layoutY = 40;
        double shift = radius + spacing/2;
        for(int i=0; i<17; i++){

                layoutX = 175 + shift * (9 - i);

            for(int j=0; j<17; j++){
                this.board[j][i].setCenterX(layoutX);
                this.board[j][i].setCenterY(layoutY);
                layoutX += 2*radius + spacing;

            }
            layoutY += 2*radius;

        }

        startButton.setVisible(false);

    }


    //todo implement correctly this method when client get info about board from server
    private void convertTableToTable(SixPointedStar board){
        this.board = new Circle[17][17];
        for(int i=0; i< 17; i++){
            for(int j=0; j<17; j++){
                if(board.getOneField(j, i) instanceof NoField){
                    this.board[j][i] = new Circle(radius);
                    this.board[j][i].setStroke(backgroundColor);
                    this.board[j][i].setFill(backgroundColor);
                    pane.getChildren().add(this.board[j][i]);
                }else if(board.getOneField(j,i) instanceof EmptyField){
                    this.board[j][i] = new Circle(radius);
                    this.board[j][i].setStroke(Color.BLACK);
                    this.board[j][i].setFill(Color.WHITE);
                    pane.getChildren().add(this.board[j][i]);
                }else if(board.getOneField(j, i) instanceof Pawn){
                    this.board[j][i] = new Circle(radius);
                    this.board[j][i].setStroke(Color.BLACK);
                    this.board[j][i].setFill(Color.GREEN);
                    pane.getChildren().add(this.board[j][i]);
                }
            }
        }

    }

    //todo temporary method to check the correct drawing
    private void setPawns(SixPointedStar board){
        FieldCreator fieldCreator = new PawnCreator();

        for(int i = 0; i < 4; ++i) {
            for(int j = 3; j >= i; --j) {
                board.setOneField(fieldCreator.createField(j, 4 + i));
                board.getOneField(j, 4 + i).setBoard(board);
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawBoard();
    }
}
