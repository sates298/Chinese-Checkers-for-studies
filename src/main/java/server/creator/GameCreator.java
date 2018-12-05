package server.creator;

import server.Server;
import server.creator.boardCreator.BoardCreator;
import server.creator.boardCreator.SixPointedStarCreator;
import server.creator.exception.WrongBoardTypeException;
import server.creator.exception.WrongMovementTypeException;
import server.creator.movementCreator.MainMovementCreator;
import server.creator.movementCreator.MovementCreator;
import server.game.Game;
import server.player.Player;

import java.util.ArrayList;
import java.util.List;

public class GameCreator {
    private MovementCreator movementCreator;
    private BoardCreator boardCreator;

    public Game createGame(String boardType, String movementType) throws WrongMovementTypeException, WrongBoardTypeException {
        Game game = new Game();
        List<Player> players = new ArrayList<>();

        if(boardType.equals("SixPointedStar")){
            boardCreator = new SixPointedStarCreator();
        }else{
            throw new WrongBoardTypeException();
        }

        if(movementType.equals("main")){
            movementCreator = new MainMovementCreator();
        }else{
            throw new WrongMovementTypeException();
        }

        game.setBoard(boardCreator.createBoard());
        game.setMovement(movementCreator.createMovement());

        int numberOfGames = Server.getInstance().getGames().size();
        if(numberOfGames == 0){
            game.setGameId(1);
        }else{
            game.setGameId(Server.getInstance().getGames().get(numberOfGames).getGameId() + 1);
        }

        Server.getInstance().getGames().add(game);

        return game;
    }
}
