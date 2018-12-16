package client.drawableBoard;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class SixPointedStarDraw implements Draw {

    @Override
    public void drawBoard(Pane pane, DrawableField[][] board) {
        pane.getChildren().clear();
        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        double radius = 15;
        double layoutX , layoutY = 40;
        double spacing = 3;
        double shift = radius + spacing /2;
        for(int i=0; i<17; i++){

            layoutX = 175 + shift * (9 - i);

            for(int j=0; j<17; j++){
                pane.getChildren().add(board[i][j]);
                board[i][j].setRadius(radius);
                board[j][i].setCenterX(layoutX);
                board[j][i].setCenterY(layoutY);
                layoutX += 2*radius + spacing;

            }
            layoutY += 2*radius;

        }
    }
}
