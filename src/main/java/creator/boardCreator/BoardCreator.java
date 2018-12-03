package creator.boardCreator;

import board.Board;
import creator.fieldCreator.FieldCreator;

public abstract class BoardCreator {
    protected FieldCreator fieldCreator;

    public abstract Board createBoard();
}
