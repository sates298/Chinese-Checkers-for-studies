package client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

public class SixPointedStarController {

    private Circle[][] board;

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
    public void exit(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void endTurn(){
        drawBoard();
    }



    public void drawBoard(){
        //todo this part is only to check correct drawing
        SixPointedStar star = (SixPointedStar) (new SixPointedStarCreator()).createBoard();
        setPawns(star);
        /////////
        convertTableToTable(star);

        int layoutX , layoutY = 100;
        for(int i=0; i<17; i++){
            layoutX = 100;
            for(int j=0; j<17; j++){
                this.board[j][i].setCenterX(layoutX);
                this.board[j][i].setCenterY(layoutY);
                layoutX +=30;
            }
            layoutY += 30;
        }

    }


    //todo implement correctly this method when client get info about board from server
    private void convertTableToTable(SixPointedStar board){
        this.board = new Circle[17][17];
        for(int i=0; i< 17; i++){
            for(int j=0; j<17; j++){
                if(board.getOneField(j, i) instanceof NoField){
                    this.board[j][i] = new Circle(15);
                    this.board[j][i].setStroke(Color.BLACK);
                    this.board[j][i].setFill(Color.WHITE);
                    pane.getChildren().add(this.board[j][i]);
                }else if(board.getOneField(j,i) instanceof EmptyField){
                    this.board[j][i] = new Circle(15);
                    this.board[j][i].setStroke(Color.GREEN);
                    this.board[j][i].setFill(Color.WHITE);
                    pane.getChildren().add(this.board[j][i]);
                }else if(board.getOneField(j, i) instanceof Pawn){
                    this.board[j][i] = new Circle(15);
                    this.board[j][i].setStroke(Color.BLACK);
                    this.board[j][i].setFill(Color.GREEN);
                    pane.getChildren().add(this.board[j][i]);
                }
            }
        }

    }

    //todo temporary method to check correct drawing
    private void setPawns(SixPointedStar board){
        FieldCreator fieldCreator = new PawnCreator();

        for(int i = 0; i < 4; ++i) {
            for(int j = 3; j >= i; --j) {
                board.setOneField(fieldCreator.createField(j, 4 + i));
                board.getOneField(j, 4 + i).setBoard(board);
            }
        }
    }


}
