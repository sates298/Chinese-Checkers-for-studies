package serverTests.creatorTests;

import org.junit.After;
import org.junit.Test;
import server.creator.fieldCreator.EmptyFieldCreator;
import server.creator.fieldCreator.FieldCreator;
import server.creator.fieldCreator.NoFieldCreator;
import server.creator.fieldCreator.PawnCreator;
import server.field.EmptyField;
import server.field.Field;
import server.field.NoField;
import server.field.Pawn;

import static junit.framework.Assert.assertTrue;

public class FieldCreatorTest {
    private Field actual;
    private FieldCreator creator;

    @After
    public void tearDown() throws Exception{
        actual = null;
        creator = null;
    }

    @Test
    public void noFieldCreatorTest(){
        creator = new NoFieldCreator();
        actual = creator.createField(5, 3);
        assertTrue(actual instanceof NoField &&
                5 == actual.getX() &&
                3 == actual.getY()
        );
    }

    @Test
    public void emptyFieldCreatorTest(){
        creator = new EmptyFieldCreator();
        actual = creator.createField(4, 5);
        assertTrue(actual instanceof EmptyField &&
                4 == actual.getX() &&
                5 == actual.getY()
        );
    }

    @Test
    public void pawnCreatorTest(){
        creator = new PawnCreator();
        actual = creator.createField(1, 2);
        assertTrue(actual instanceof Pawn &&
                1 == actual.getX() &&
                2 == actual.getY()
        );
    }
}
