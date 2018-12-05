package server.creator.boardCreator;

import server.board.Board;
import server.creator.fieldCreator.FieldCreator;

public abstract class BoardCreator {
    protected FieldCreator fieldCreator;

    public abstract Board createBoard();
}
