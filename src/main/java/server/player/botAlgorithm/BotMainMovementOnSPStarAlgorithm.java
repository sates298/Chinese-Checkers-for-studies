package server.player.botAlgorithm;

import server.board.Board;
import server.field.EmptyField;
import server.movement.Movement;
import server.player.Bot;

public class BotMainMovementOnSPStarAlgorithm extends BotSixPointedStarAlgorithm {
    @Override
    public void checkMoves(Bot b) {
        b.getTargets().clear();
        Board board = b.getGame().getBoard();
        Movement move = b.getGame().getMovement();
        int sizeX = board.sizeX();
        int sizeY = board.sizeY();

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (move.checkMove(b.getChosenPawn(), board.getOneField(i, j))) {
                    b.getTargets().add((EmptyField) board.getOneField(i, j));
                    b.setMovable(true);
                }
            }
        }

    }
}