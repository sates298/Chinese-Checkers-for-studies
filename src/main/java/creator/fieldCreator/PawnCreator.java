package creator.fieldCreator;

import field.Field;
import field.Pawn;

public class PawnCreator implements FieldCreator {
    public Field createField(int x, int y) {
        return new Pawn(x,y);
    }
}
