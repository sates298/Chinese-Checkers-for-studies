package client.drawableBoard;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DrawableField extends Circle {
  public DrawableField (String colorName, String type) {
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
            this.setFill(Color.RED);
            this.setStroke(Color.BLACK);
            break;
          case "\"GREEN\"":
            this.setFill(Color.GREEN);
            this.setStroke(Color.BLACK);
            break;
          case "\"BLACK\"":
            this.setFill(Color.BLACK);
            this.setStroke(Color.BLACK);
            break;
          case "\"BLUE\"":
            this.setFill(Color.BLUE);
            this.setStroke(Color.BLACK);
            break;
          case "\"YELLOW\"":
            this.setFill(Color.YELLOW);
            this.setStroke(Color.BLACK);
            break;
          case "\"PURPLE\"":
            this.setFill(Color.PURPLE);
            this.setStroke(Color.BLACK);
            break;
        }
        break;
    }
  }
}
