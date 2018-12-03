package creator.fieldCreator;

import creator.fieldCreator.FieldCreator;
import field.Field;
import field.NoField;

public class NoFieldCreator implements FieldCreator {
    public Field createField(int x, int y) {
        return new NoField(x,y);
    }
}
