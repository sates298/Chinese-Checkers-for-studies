package serverTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.creator.GameCreator;
import server.exception.*;
import server.field.EmptyField;
import server.field.NoField;
import server.field.Pawn;
import server.game.Game;
import server.player.Player;

public class MoveTest {
  private Game testedGame;
  private GameCreator gameCreator;
  Player p1;
  Player p2;


  @Before
  public void prepareGameToTest() throws WrongMovementTypeException, WrongBoardTypeException,
      BoardSideUsedException, GameFullException, ColorUsedException {
    this.gameCreator = new GameCreator();
    this.testedGame = gameCreator.createGame("\"SixPointedStar\"", "\"main\"",
        2, 10);

    p1 = this.testedGame.getController().addPlayer("\"TOP\"", "\"RED\"");
    p2 = this.testedGame.getController().addPlayer("\"BOTTOM\"", "\"GREEN\"");

    this.testedGame.getController().startGame();
  }

  @Test(expected = ForbiddenActionException.class)
  public void testCanNotMoveEnemyPawn() throws ForbiddenMoveException, ForbiddenActionException {
    Pawn enemyPawn = p2.getPawns().get(0);
    this.testedGame.getBoard().setOneField(new NoField(enemyPawn.getX()+1, enemyPawn.getY()));

    this.testedGame.getController().move(p1.getId(), enemyPawn.getX()+1, enemyPawn.getY(),
        enemyPawn.getX(), enemyPawn.getY());
  }

  @Test(expected = ForbiddenMoveException.class)
  public void testCanNotMoveToEnemyPawnLocation() throws ForbiddenMoveException, ForbiddenActionException {
    Pawn friendlyPawn = p1.getPawns().get(0);
    Pawn enemyPawn = new Pawn(friendlyPawn.getX()+1, friendlyPawn.getY());
    enemyPawn.setOwner(p2);
    this.testedGame.getBoard().setOneField(enemyPawn);
    this.testedGame.getController().move(p1.getId(), friendlyPawn.getX(), friendlyPawn.getY(),
        enemyPawn.getX(), enemyPawn.getY());
  }
  @Test(expected = ForbiddenMoveException.class)
  public void testCanNotMoveOToNoField() throws ForbiddenMoveException, ForbiddenActionException {
    Pawn friendlyPawn = p1.getPawns().get(0);
    this.testedGame.getBoard().setOneField(new NoField(friendlyPawn.getX()+1, friendlyPawn.getY()));
    this.testedGame.getController().move(p1.getId(), friendlyPawn.getX(), friendlyPawn.getY(),
        friendlyPawn.getX()+1, friendlyPawn.getY());
  }

  @Test
  public void testCanMoveOneFieldHorizontallyRight() throws ForbiddenMoveException, ForbiddenActionException {
    Pawn friendlyPawn = p1.getPawns().get(0);
    this.testedGame.getBoard().setOneField(new EmptyField(friendlyPawn.getX()+1, friendlyPawn.getY()));
    this.testedGame.getController().move(p1.getId(), friendlyPawn.getX(), friendlyPawn.getY(),
        friendlyPawn.getX()+1, friendlyPawn.getY());
  }

  @Test
  public void testCanMoveOneFieldHorizontallyLeft() throws ForbiddenMoveException, ForbiddenActionException {
    Pawn friendlyPawn = p1.getPawns().get(0);
    this.testedGame.getBoard().setOneField(new EmptyField(friendlyPawn.getX()-1, friendlyPawn.getY()));
    this.testedGame.getController().move(p1.getId(), friendlyPawn.getX(), friendlyPawn.getY(),
        friendlyPawn.getX()-1, friendlyPawn.getY());
  }
  @Test
  public void testCanMoveOneFieldBackwards() throws ForbiddenMoveException, ForbiddenActionException {
    Pawn friendlyPawn = p1.getPawns().get(0);
    this.testedGame.getBoard().setOneField(new EmptyField(friendlyPawn.getX(), friendlyPawn.getY()+1));
    this.testedGame.getController().move(p1.getId(), friendlyPawn.getX(), friendlyPawn.getY(),
        friendlyPawn.getX(), friendlyPawn.getY()+1);
  }
  @Test
  public void testCanMoveOneFieldForward() throws ForbiddenMoveException, ForbiddenActionException {
    Pawn friendlyPawn = p1.getPawns().get(5); // cannot take oth because we would get out of bounds of the array
    this.testedGame.getBoard().setOneField(new EmptyField(friendlyPawn.getX(), friendlyPawn.getY() - 1));
    this.testedGame.getController().move(p1.getId(), friendlyPawn.getX(), friendlyPawn.getY(),
        friendlyPawn.getX(), friendlyPawn.getY() - 1);
  }
  @Test
  public void testCanMoveDiagonallyForward() throws ForbiddenMoveException, ForbiddenActionException {
    Pawn friendlyPawn = p1.getPawns().get(5);
    this.testedGame.getBoard().setOneField(new EmptyField(friendlyPawn.getX()-1, friendlyPawn.getY() - 1));
    this.testedGame.getController().move(p1.getId(), friendlyPawn.getX(), friendlyPawn.getY(),
        friendlyPawn.getX()-1, friendlyPawn.getY() - 1);
  }
  @Test
  public void testCanMoveDiagonallyBackwards() throws ForbiddenMoveException, ForbiddenActionException {
    Pawn friendlyPawn = p1.getPawns().get(5);
    this.testedGame.getBoard().setOneField(new EmptyField(friendlyPawn.getX()+1, friendlyPawn.getY() + 1));
    this.testedGame.getController().move(p1.getId(), friendlyPawn.getX(), friendlyPawn.getY(),
        friendlyPawn.getX()+1, friendlyPawn.getY() + 1);
  }

  @After
  public void cleanup() {
    this.testedGame.getController().endGame();
  }
}
