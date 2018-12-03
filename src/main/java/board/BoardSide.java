package board;

import field.Field;

import java.util.List;

public interface BoardSide {
    List<Field> getArea(Board board);
}
