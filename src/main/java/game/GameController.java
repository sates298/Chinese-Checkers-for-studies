package game;

import field.Field;
import field.Pawn;
import player.Player;

public class GameController {
  private Game actual;

  public GameController(Game actual) {
    this.actual = actual;
  }

  public void endGame() {

  }
  public void move(Player player, Pawn pawn, Field target) {
    this.actual.getMovement().move(pawn, target);
  }
  public void skipTurn(Player player) {

  }
  public void endTurn(Player player)  {

  }
}
