package serverTests.creatorTests;

import org.junit.After;
import server.creator.movementCreator.MovementCreator;
import server.movement.Movement;

public class MovementCreatorTest {
    Movement move;
    MovementCreator creator;

    @After
    public void tearDown(){
        move = null;
        creator = null;
    }
}
