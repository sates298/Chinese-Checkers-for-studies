package serverTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import server.Server;
import server.board.SixPointedStarSide;
import server.creator.GameCreator;
import server.exception.*;
import server.field.EmptyField;
import server.field.Field;
import server.field.NoField;
import server.field.Pawn;
import server.game.Game;
import server.player.Bot;
import server.player.MoveToken;
import server.player.Player;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class GameControllerTest {

    private Game game;

    @Before
    public void prepareTestingGame() throws WrongMovementTypeException, WrongBoardTypeException {
        GameCreator creator = new GameCreator();
        this.game = creator.createGame("\"SixPointedStar\"",
                "\"main\"",
                2, 10);
    }

    @After
    public void tearDown() {
        if (Server.getInstance().getGames().contains(game)) {
            this.game.getController().endGame();
        }
    }

    //block of startGame() tests
    @Test
    public void testStartGameWithoutBots() throws BoardSideUsedException, GameFullException, ColorUsedException {
        this.game.getController().addPlayer("\"TOP\"", "\"RED\"");
        this.game.getController().addPlayer("\"BOTTOM\"", "\"BLUE\"");
        this.game.getController().startGame();
        assertTrue(MoveToken.ALLOW.equals(game.getController().getCurrentTurnPlayer().getMoveToken()) &&
                game.getController().isStarted() &&
                !(game.getPlayers().get(0) instanceof Bot) &&
                !(game.getPlayers().get(1) instanceof Bot)
        );
    }

    @Test
    public void testStartNonFullGame() throws BoardSideUsedException, GameFullException, ColorUsedException {
        this.game.getController().addPlayer("\"TOP\"", "\"RED\"");
        this.game.getController().startGame();
        assertTrue(MoveToken.ALLOW.equals(game.getController().getCurrentTurnPlayer().getMoveToken()) &&
                game.getController().isStarted() &&
                !(game.getPlayers().get(0) instanceof Bot) &&
                game.getPlayers().get(1) instanceof Bot
        );

    }

    //endGame() method test
    @Test
    public void endGame() {
        this.game.getController().endGame();
        assertFalse(this.game.getController().isStarted() ||
                Server.getInstance().getGames().contains(game)
        );
    }

    //block of endTurn() tests
    @Test
    public void testNormalEndTurn() throws BoardSideUsedException, GameFullException,
            ColorUsedException, ForbiddenActionException {
        this.game.getController().addPlayer("\"TOP\"", "\"RED\"");
        this.game.getController().addPlayer("\"BOTTOM\"", "\"BLUE\"");
        this.game.getController().startGame();
        Player first = this.game.getController().getCurrentTurnPlayer();
        this.game.getController().endTurn(first.getId());
        Player second = this.game.getController().getCurrentTurnPlayer();
        this.game.getController().endTurn(second.getId());
        assertTrue(!first.equals(second) &&
                first.equals(this.game.getController().getCurrentTurnPlayer())
        );
    }

    @Test
    public void testLastEndTurn() throws BoardSideUsedException, GameFullException,
            ColorUsedException, ForbiddenActionException {
        this.game.getController().addPlayer("\"TOP\"", "\"RED\"");
        this.game.getController().addPlayer("\"BOTTOM\"", "\"BLUE\"");
        this.game.getController().startGame();
        Player prev = this.game.getController().getCurrentTurnPlayer();
        this.game.getController().endTurn(prev.getId());
        prev.setMoveToken(MoveToken.WINNER);
        Player curr = this.game.getController().getCurrentTurnPlayer();
        this.game.getController().endTurn(curr.getId());
        assertFalse(this.game.getController().isStarted());
    }

    @Test(expected = ForbiddenActionException.class)
    public void testWrongPlayerEndTurn() throws ForbiddenActionException, BoardSideUsedException,
            GameFullException, ColorUsedException {
        this.game.getController().addPlayer("\"TOP\"", "\"RED\"");
        this.game.getController().startGame();
        this.game.getController().endTurn(
                this.game.getController().getCurrentTurnPlayer().getId() + 1);
    }

    //block of addPlayer() tests
    @Test
    public void testAddPlayer() throws BoardSideUsedException, GameFullException, ColorUsedException {
        Player p = this.game.getController().addPlayer("\"TOP\"", "\"BLUE\"");
        List<Field> list = SixPointedStarSide.TOP.getArea(game.getBoard());
        boolean isTheSame = false;
        for (Field f : list) {
            isTheSame = p.getPawns().contains(f);
            if (!isTheSame) {
                break;
            }
        }
        assertTrue(isTheSame && this.game.getPlayers().contains(p));
    }

    @Test(expected = BoardSideUsedException.class)
    public void testChoosingUsedSide() throws BoardSideUsedException, GameFullException, ColorUsedException {
        this.game.getController().addPlayer("\"TOP\"", "\"BLUE\"");
        this.game.getController().addPlayer("\"TOP\"", "\"RED\"");
    }

    @Test(expected = ColorUsedException.class)
    public void testChoosingUsedColor() throws BoardSideUsedException, GameFullException, ColorUsedException {
        this.game.getController().addPlayer("\"TOP\"", "\"BLUE\"");
        this.game.getController().addPlayer("\"BOTTOM\"", "\"BLUE\"");
    }

    @Test(expected = GameFullException.class)
    public void testTooManyPlayersInGame() throws BoardSideUsedException, GameFullException, ColorUsedException {
        this.game.getController().addPlayer("\"TOP\"", "\"BLUE\"");
        this.game.getController().addPlayer("\"BOTTOM\"", "\"RED\"");
        this.game.getController().addPlayer("\"LEFT_TOP\"", "\"PURPLE\"");
    }


//
//    @Test
//    public void getIdColorMap() {
//    }
//
//    @Test
//    public void getEnabledColors() {
//    }
//
//    @Test
//    public void getEnabledSides() {
//    }

}