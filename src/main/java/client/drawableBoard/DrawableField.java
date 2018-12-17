package client.drawableBoard;

import client.ClientBase;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


public class DrawableField extends Circle {
  private int x;
  private int y;
  private String type;
  private Paint mainColor;

  public Paint getMainColor() {
    return mainColor;
  }

  public DrawableField (String colorName, String type, int x, int y) {
    this.x = x;
    this.y = y;
    this.type = type;

    switch (type) {
      case "\"NoField\"":
        // set stroke and fill to white so invisible
        this.setStroke(Color.WHITE);
        this.setFill(Color.WHITE);
        break;
      case "\"EmptyField\"":
        this.setStroke(Color.BLACK);
        this.setFill(Color.WHITE);
        break;
      default:
        switch (colorName) {
          // player's pawn
          case "\"RED\"":
            mainColor = Color.RED;
            this.setFill(Color.RED);
            this.setStroke(Color.BLACK);
            break;
          case "\"GREEN\"":
            mainColor = Color.GREEN;
            this.setFill(Color.GREEN);
            this.setStroke(Color.BLACK);
            break;
          case "\"BLACK\"":
            mainColor = Color.BLACK;
            this.setFill(Color.BLACK);
            this.setStroke(Color.BLACK);
            break;
          case "\"BLUE\"":
            mainColor = Color.BLUE;
            this.setFill(Color.BLUE);
            this.setStroke(Color.BLACK);
            break;
          case "\"YELLOW\"":
            mainColor = Color.YELLOW;
            this.setFill(Color.YELLOW);
            this.setStroke(Color.BLACK);
            break;
          case "\"PURPLE\"":
            mainColor = Color.PURPLE;
            this.setFill(Color.PURPLE);
            this.setStroke(Color.BLACK);
            break;
        }
        break;
    }
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public String getType() {
    return type;
  }

}
