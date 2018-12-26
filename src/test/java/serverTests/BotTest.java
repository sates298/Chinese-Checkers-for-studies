package serverTests;

import org.junit.Before;
import org.junit.Test;
import server.board.Board;
import server.creator.GameCreator;
import server.exception.*;
import server.game.Game;
import server.player.Bot;
import server.player.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class BotTest {
  private Game game;
  private Player player;
  private Bot bot;

  @Before
  public void prepareGameForTests() throws WrongMovementTypeException, WrongBoardTypeException, BoardSideUsedException, GameFullException, ColorUsedException {
    GameCreator gc = new GameCreator();
    this.game = gc.createGame("\"SixPointedStar\"", "\"main\"", 2, 10);
    player = this.game.getController().addPlayer("\"TOP\"", "\"RED\"");
    this.game.getController().startGame();
    bot = (Bot)this.game.getPlayers().get(1);
  }


  @Test
  public void testBotMovedInItsTurn() throws ForbiddenActionException {
    String boardBefore = this.game.getBoard().fieldsToString();
    this.game.getController().endTurn(player.getId());
    String boardAfter = this.game.getBoard().fieldsToString();
    assertNotSame(boardAfter, boardBefore);
  }

}
