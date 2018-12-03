package movement;

import field.EmptyField;
import field.Field;
import field.Pawn;

public class MainMovement extends Movement {

  @Override
  public void move(Pawn pawn, Field target) {
    // temporarily we only use atomic moves
    if (checkMove(pawn, target)) {
      moveOne(pawn, (EmptyField)target);
    }
  }

  @Override
  public boolean checkMove(Pawn pawn, Field target) {
    return checkMoveOne(pawn, target)
        || checkJump(pawn, target);
  }


  public void moveOne(Pawn pawn, EmptyField target) {
    pawn.setX(target.getX());
    pawn.setY(target.getY());
    target.setX(pawn.getX());
    target.setY(pawn.getY());
    // TODO swap fields in array
  }

  public void jump(Pawn pawn, Field target) {
    // TODO implement jumping
  }

  public boolean checkMoveOne(Pawn pawn, Field target) {
    // this method only checks "atomic" moves
    // first check if field is empty
    if (!(target instanceof EmptyField)) {
      return false;
    }
    // all following checks assume that we sue standard cartesian coordinates
    //forward move
    if (target.getY() == pawn.getY()+1) {
      return pawn.getX() - target.getX() == 0
          || pawn.getX() - target.getX() == 1;
    }
    // backwards move
    if (target.getY() == pawn.getY()-1) {
      return pawn.getX() - target.getX() == 0
          || pawn.getX() - target.getX() == -1;
    }
    // horizontal move
    if (target.getY() == target.getY()) {
      return pawn.getX() - target.getX() == 1
          || pawn.getX() - target.getX() == -1;
    }
    // any other move is forbidden
    return false;
  }

  private boolean checkJump(Pawn pawn, Field target) {
    // todo implement checks for correct jumps
    return false;
  }
}
