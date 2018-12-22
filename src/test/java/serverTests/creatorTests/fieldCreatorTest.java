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


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class fieldCreatorTest {
    Field actual;
    FieldCreator creator;

    @After
    public void tearDown() throws Exception{
        actual = null;
        creator = null;
    }

    @Test
    public void noFieldCreatorTest(){
        creator = new NoFieldCreator();
        actual = creator.createField(5, 3);
        assertEquals(5, actual.getX());
        assertEquals(3, actual.getY());
        assertTrue(actual instanceof NoField);
    }

    @Test
    public void emptyFieldCreatorTest(){
        creator = new EmptyFieldCreator();
        actual = creator.createField(4, 5);
        assertEquals(4, actual.getX());
        assertEquals(5, actual.getY());
        assertTrue(actual instanceof EmptyField);
    }

    @Test
    public void pawnCreatorTest(){
        creator = new PawnCreator();
        actual = creator.createField(1, 2);
        assertEquals(4, actual.getX());
        assertEquals(5, actual.getY());
        assertTrue(actual instanceof Pawn);
    }
}
