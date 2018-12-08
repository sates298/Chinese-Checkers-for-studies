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
import java.util.Optional;
import java.util.stream.Collectors;

public class GameController {
  private Game actual;
  private Player currentTurnPlayer;
  private ListIterator<Player> playerListIterator;

  public GameController(Game actual) {
    this.actual = actual;
  }

  public Player getCurrentTurnPlayer() {
    return currentTurnPlayer;
  }

  public void startGame() {
    // sort players by starting sides (add values to  starting sides)
    this.actual.getPlayers().sort(Comparator.comparingInt((Player p) -> p.getStartingSide().getNum()));
    this.playerListIterator = this.actual.getPlayers().listIterator();
    // set player's id to his index in list (done in addPlayer() )
    //this.actual.getPlayers().forEach(p -> p.setId(this.actual.getPlayers().indexOf(p)));

    currentTurnPlayer = playerListIterator.next();
    //allow current player to move
    currentTurnPlayer.setMoveToken(1);
  }

  public void endGame() {

  }
  public void move(int playerId, int pawnX, int pawnY, int targetX, int targetY) throws ForbiddenMoveException, ForbiddenActionException {
    if (playerId != this.currentTurnPlayer.getId() || this.currentTurnPlayer.getMoveToken() == 0) {
      throw new ForbiddenActionException();
    }
    // find pawn and target  by coordinates
    Optional<Pawn> optionalPawn = this.currentTurnPlayer.getPawns().stream().filter(p -> p.getX() == pawnX && p.getY()== pawnY).findFirst();
    Field target= this.actual.getBoard().getOneField(targetX, targetY);

    if (optionalPawn.isPresent()) {
      this.actual.getMovement().move(optionalPawn.get(), target);
    } else {
      throw new ForbiddenActionException();
    }
  }

  //todo this method will be unused, because it will work the same way as endTurn()
  public void skipTurn(int playerId) throws ForbiddenActionException {
    endTurn(playerId);
  }
  public void endTurn(int playerId) throws ForbiddenActionException {
    if (this.currentTurnPlayer.getId() != playerId) {
      throw new ForbiddenActionException();
    }

    //forbid current player to move if is not a winner yet
    if(checkWinCondition()){
      this.currentTurnPlayer.setMoveToken(3);
    }else{
      this.currentTurnPlayer.setMoveToken(0);
    }
    Player prev = this.currentTurnPlayer;

    // set current player to next player who has moveToken not equal 3 in player list
    do {
      if (playerListIterator.hasNext()) {
        currentTurnPlayer = playerListIterator.next();

      } else {
        playerListIterator = this.actual.getPlayers().listIterator();
        currentTurnPlayer = playerListIterator.next();
      }
      //if only one player (or no one) has move token not equal 3, end game
      if(prev.getId() == this.currentTurnPlayer.getId() ){
        endGame();
        break;
      }

    } while (currentTurnPlayer.getMoveToken() == 3);
    //if only one player has move token not equal 3, end game
    if(prev.getId() == this.currentTurnPlayer.getId() ){
      endGame();
    }
    //allow next player to move
    this.currentTurnPlayer.setMoveToken(1);
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
      p.setId(this.actual.getPlayers().indexOf(p));
      return p;

    }
    // todo improve this method, get rid of nulls, create pawns,
    return null;
  }


  private boolean checkWinCondition(){
    //Player should has minimum 1 pawn, otherwise this method shouldn't be called
    if(this.currentTurnPlayer.getStartingSide() instanceof SixPointedStarSide) {
      List<Field> winning =
              ((SixPointedStarSide)this.currentTurnPlayer.getStartingSide()).getOppositeArea((SixPointedStar) this.actual.getBoard());
      int fieldsMatches = 0;

      for (Field f : winning) {
        for (Pawn p : this.currentTurnPlayer.getPawns()) {
          if (p.equals(f)) {
            fieldsMatches++;
          }
        }
      }

      return fieldsMatches == this.currentTurnPlayer.getPawns().size();

    }
    return false;
  }

}
