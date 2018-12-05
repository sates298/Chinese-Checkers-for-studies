package server.creator.movementCreator;

import server.movement.MainMovement;
import server.movement.Movement;

public class MainMovementCreator implements MovementCreator{
    @Override
    public Movement createMovement() {
        return new MainMovement();
    }
}
