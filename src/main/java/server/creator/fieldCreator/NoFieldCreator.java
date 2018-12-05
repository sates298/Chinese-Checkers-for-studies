package server.creator.fieldCreator;

import server.field.Field;
import server.field.NoField;

public class NoFieldCreator implements FieldCreator {
    public Field createField(int x, int y) {
        return new NoField(x,y);
    }
}
