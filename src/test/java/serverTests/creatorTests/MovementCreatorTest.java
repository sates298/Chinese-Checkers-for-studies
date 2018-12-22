package serverTests.creatorTests;

import org.junit.After;
import org.junit.Test;
import server.creator.movementCreator.MainMovementCreator;
import server.creator.movementCreator.MovementCreator;
import server.movement.MainMovement;
import server.movement.Movement;

import static org.junit.Assert.assertTrue;

public class MovementCreatorTest {
    private Movement move;
    private MovementCreator creator;

    @After
    public void tearDown(){
        move = null;
        creator = null;
    }

    @Test
    public void testMainMovementCreator(){
        creator = new MainMovementCreator();
        move = creator.createMovement();
        assertTrue(move instanceof MainMovement);
    }
}
