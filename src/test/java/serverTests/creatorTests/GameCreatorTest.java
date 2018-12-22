package serverTests.creatorTests;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Server;
import server.board.SixPointedStar;
import server.creator.GameCreator;
import server.exception.WrongBoardTypeException;
import server.exception.WrongMovementTypeException;
import server.game.Game;
import server.movement.MainMovement;

import static junit.framework.Assert.assertTrue;

public class GameCreatorTest {

    String boardType;
    String moveType;
    GameCreator creator;

    @AfterClass
    public static void tearDown(){
        Server.getInstance().getGames().clear();
    }

    @Before
    public void setUp(){
        creator = new GameCreator();
    }

    @Test(expected = WrongBoardTypeException.class)
    public void wBTypeExceptionTest() throws WrongMovementTypeException, WrongBoardTypeException {
        moveType = "\"main\"";
        creator.createGame("something wrong", moveType, 2, 10);
    }

    @Test(expected = WrongMovementTypeException.class)
    public void wMTypeExceptionTest() throws WrongMovementTypeException, WrongBoardTypeException {
        boardType = "\"SixPointedStar\"";
        creator.createGame(boardType, "something wrong", 2, 10);
    }

    @Test
    public void createGameTest() throws WrongMovementTypeException, WrongBoardTypeException {
        boardType = "\"SixPointedStar\"";
        moveType = "\"main\"";
        creator.createGame(boardType, moveType, 2, 10);
        Game game = Server.getInstance().getGames().get(0);
        assertTrue(1 == game.getGameId() &&
                game.getBoard() instanceof SixPointedStar &&
                game.getPlayers().size() == 0 &&
                game.getMovement() instanceof MainMovement &&
                game.getNumberOfPawns() == 10 &&
                game.getNumberOfPlayers() == 2);
    }

}
