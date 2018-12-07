package server.game;

import server.board.BoardSide;
import server.board.SixPointedStar;
import server.board.SixPointedStarSide;
import server.exception.BoardSideUsedException;
import server.exception.ColorUsedException;
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

  //todo this method will be unused, because it will works the same way as endTurn()
  public void skipTurn(Player player) {

  }
  public void endTurn(Player player)  {

  }
  public Player addPlayer(String sideStr, String colorStr) throws GameFullException, ColorUsedException, BoardSideUsedException {
    // find used color and starting side
    List<Color> usedColors = this.actual.getPlayers().stream().map(Player::getColor).collect(Collectors.toList());
    List<BoardSide> usedSides = this.actual.getPlayers().stream().map(Player::getStartingSide).collect(Collectors.toList());


    if (this.actual.getBoard() instanceof SixPointedStar) {
      BoardSide side = SixPointedStarSide.valueOf(sideStr);
      Color color = Color.valueOf(colorStr);
      if (this.actual.getPlayers().size() >= 6) {
        throw new GameFullException();
      }

      if (usedColors.indexOf(color) >= 0) {
        throw new BoardSideUsedException();
      }
      if (usedSides.indexOf(side) >= 0) {
        throw new ColorUsedException();
      }
      if (color != null && side != null) {
        Player p = new Player(side, color);
        this.actual.getPlayers().add(p);
        return p;
      }

    }
    // todo improve this method, get rid of nulls, create pawns,
    return null;
  }
}
