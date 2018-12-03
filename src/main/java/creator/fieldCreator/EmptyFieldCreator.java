package creator.fieldCreator;

import field.EmptyField;
import field.Field;

public class EmptyFieldCreator implements FieldCreator {

    public Field createField(int x, int y) {
        return new EmptyField(x,y);
    }
}
