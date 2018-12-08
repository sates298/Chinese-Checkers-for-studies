package server.movement;


import server.exception.ForbiddenMoveException;
import server.field.Field;
import server.field.Pawn;


public interface Movement {

  void move(Pawn pawn, Field target) throws ForbiddenMoveException;
  boolean checkMove(Pawn pawn, Field target);
}
