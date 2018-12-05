package server.game;

import server.field.Field;
import server.field.Pawn;
import server.player.Player;

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
