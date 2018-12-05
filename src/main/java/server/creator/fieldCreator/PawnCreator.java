package server.creator.fieldCreator;

import server.field.Field;
import server.field.Pawn;

public class PawnCreator implements FieldCreator {
    public Field createField(int x, int y) {
        return new Pawn(x,y);
    }



}
