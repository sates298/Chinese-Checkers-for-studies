package server.game;


import server.board.BoardSide;
import server.board.SixPointedStar;
import server.board.SixPointedStarSide;
import server.exception.*;
import server.field.Field;
import server.field.Pawn;
import server.player.Color;
import server.player.Player;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class GameController {
  private Game actual;
  private Player currentTurnPlayer;
  private ListIterator<Player> playerListIterator;

  public GameController(Game actual) {
    this.actual = actual;
  }

  public void startGame() {
    // sort players by starting sides (add values to  starting sides)
    this.actual.getPlayers().sort(Comparator.comparingInt((Player p) -> p.getStartingSide().getNum()));
    this.playerListIterator = this.actual.getPlayers().listIterator();
    // set player's id to his index in list
    this.actual.getPlayers().forEach(p -> p.setId(this.actual.getPlayers().indexOf(p)));
    currentTurnPlayer = playerListIterator.next();
  }

  public void endGame() {

  }
  public void move(Player player, Pawn pawn, Field target) throws ForbiddenMoveException {
    this.actual.getMovement().move(pawn, target);
  }

  //todo this method will be unused, because it will work the same way as endTurn()
  public void skipTurn(int playerId) throws ForbiddenActionException {
    endTurn(playerId);

  }
  public void endTurn(int playerId) throws ForbiddenActionException {
    if (this.currentTurnPlayer.getId() != playerId) {
      throw new ForbiddenActionException();
    }
    // set current player to next player in player list
    if (playerListIterator.hasNext()) {
      currentTurnPlayer = playerListIterator.next();
    } else {
      playerListIterator = this.actual.getPlayers().listIterator();
      currentTurnPlayer = playerListIterator.next();
    }
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

      if (usedSides.indexOf(side) >= 0) {
        throw new BoardSideUsedException();
      }
      if (usedColors.indexOf(color) >= 0) {
        throw new ColorUsedException();
      }
      Player p = new Player(side, color);
      this.actual.getPlayers().add(p);
      return p;

    }
    // todo improve this method, get rid of nulls, create pawns,
    return null;
  }
}
