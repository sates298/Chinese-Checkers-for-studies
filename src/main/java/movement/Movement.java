package movement;

import board.Board;
import field.Field;
import field.Pawn;
import player.Player;

public abstract class Movement {
  private Board board;
  private Player player;

  public void setPlayer(Player player) {
    this.player = player;
  }

  public abstract void move(Pawn pawn, Field target);
  public abstract boolean checkMove(Pawn pawn, Field target);
}
