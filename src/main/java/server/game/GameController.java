package server.game;

import server.board.BoardSide;
import server.board.SixPointedStar;
import server.board.SixPointedStarSide;
import server.exception.ForbiddenMoveException;
import server.exception.GameFullException;
import server.field.Field;
import server.field.Pawn;
import server.player.Color;
import server.player.Player;

import java.util.List;
import java.util.stream.Collectors;

public class GameController {
  private Game actual;

  public GameController(Game actual) {
    this.actual = actual;
  }

  public void endGame() {

  }
  public void move(Player player, Pawn pawn, Field target) throws ForbiddenMoveException {
    this.actual.getMovement().move(pawn, target);
  }
  public void skipTurn(Player player) {

  }
  public void endTurn(Player player)  {

  }
  public Player addPlayer() throws GameFullException {
    // find used color and starting side
    List<Color> usedColors = this.actual.getPlayers().stream().map(Player::getColor).collect(Collectors.toList());
    List<BoardSide> usedSides = this.actual.getPlayers().stream().map(Player::getStartingSide).collect(Collectors.toList());

    if (this.actual.getBoard() instanceof SixPointedStar) {
      if (this.actual.getPlayers().size() >= 6) {
        throw new GameFullException();
      }

      // pick first free board side and color
      Color color = null;
      BoardSide side = null;

      for (Color c : Color.values()) {
        if (usedColors.indexOf(c) < 0) {
          color = c;
        }
      }
      for (BoardSide bs : SixPointedStarSide.values()) {
        if (usedSides.indexOf(bs) < 0) {
          side = bs;
        }
      }
      if (color != null && side != null) {
        Player p = new Player(side, color);
        this.actual.getPlayers().add(p);
        return p;
      }

    }
    // todo improve this method, get rid of nulls, create pawns, give user an opportunity to choose the side and the color
    return null;
  }
}
