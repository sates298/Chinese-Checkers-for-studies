package server.board;

import server.field.Field;

import java.util.List;

public interface BoardSide {
    List<Field> getArea(Board board);
    int getNum();
}
