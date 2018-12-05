package server.creator.fieldCreator;

import server.field.EmptyField;
import server.field.Field;

public class EmptyFieldCreator implements FieldCreator {

    public Field createField(int x, int y) {
        return new EmptyField(x,y);
    }
}
