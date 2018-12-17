package server.movement;

import server.board.Board;
import server.board.SixPointedStar;
import server.board.SixPointedStarSide;
import server.exception.ForbiddenMoveException;
import server.field.EmptyField;
import server.field.Field;
import server.field.Pawn;
import server.player.MoveToken;
import server.player.Player;

import java.util.List;

public class MainMovement implements Movement {

    @Override
    public void move(Pawn pawn, Field target) throws ForbiddenMoveException {

        if (checkMove(pawn, target)) {
            if(checkJump(pawn, target)){
                pawn.getOwner().setMoveToken(MoveToken.ONLY_JUMP);
            }else {
                pawn.getOwner().setMoveToken(MoveToken.FORBID);
            }
            int pawnX = pawn.getX();
            int pawnY = pawn.getY();
            pawn.setX(target.getX());
            pawn.setY(target.getY());
            target.setX(pawnX);
            target.setY(pawnY);

            pawn.getBoard().setOneField(pawn);
            pawn.getBoard().setOneField(target);

        } else {
            throw new ForbiddenMoveException();
        }

    }

    @Override
    public boolean checkMove(Pawn pawn, Field target) {
        //first check if target is empty
        if (!(target instanceof EmptyField)) {
            return false;
        }

        //later check if board place allow to move
        // todo !!
        // I overrided  checkMoveForBoard to use type SixPointedStar  but getBoard still returns Board
   /*     if (!checkMoveForBoard(pawn.getBoard(), pawn, (EmptyField) target)) {
            return false;
        }*/

        return checkMoveOne(pawn, target)
                || checkJump(pawn, target) ;
    }

    private boolean checkMoveOne(Pawn pawn, Field target) {
        // this method only checks "atomic" moves

        // all following checks assume that we sue flipped cartesian coordinates (ys raise as we go down)
        //forward move
        if (target.getY() == pawn.getY() - 1) {
            return pawn.getX() - target.getX() == 0
                    || pawn.getX() - target.getX() == 1;
        }
        // backwards move
        if (target.getY() == pawn.getY() + 1) {
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

        // vertical jump
        if (pawn.getX() == target.getX()) {
            return (pawn.getY() - target.getY() == 2
                        && pawn.getBoard().isPawn(pawn.getX(), pawn.getY() - 1))
                    || (pawn.getY() - target.getY() == -2
                            && pawn.getBoard().isPawn(pawn.getX(), pawn.getY() + 1));
        }
        //horizontal jump
        if (pawn.getY() == target.getY()) {
            return (pawn.getX() - target.getX() == 2
                        && pawn.getBoard().isPawn(pawn.getX() - 1, pawn.getY()))
                    || (pawn.getX() - target.getX() == -2
                            && pawn.getBoard().isPawn(pawn.getX() + 1, pawn.getY()));
        }
        // '\' jump
        return (pawn.getY() - target.getY() == -2
                    && pawn.getX() - target.getX() == -2
                    && pawn.getBoard().isPawn(pawn.getX() + 1, pawn.getY() + 1))
                || (pawn.getY() - target.getY() == 2
                        && pawn.getX() - target.getX() == 2
                        && pawn.getBoard().isPawn(pawn.getX() - 1 , pawn.getY() - 1));


    }

    private boolean checkMoveForBoard(Board board, Pawn pawn, EmptyField target) {
        //if (board instanceof SixPointedStar) {
            List<Field> temp = ((SixPointedStarSide) pawn.getOwner().getStartingSide()).getOppositeArea((SixPointedStar)board);
            return temp.indexOf(pawn) >= 0 && temp.indexOf(target) >= 0;
        //}

        //return false;

    }

    private boolean checkMoveForBoard(SixPointedStar board, Pawn pawn, EmptyField target) {
        List<Field> temp = ((SixPointedStarSide) pawn.getOwner().getStartingSide()).getOppositeArea(board);

        return temp.indexOf(pawn) >= 0 && temp.indexOf(target) >= 0;
    }
}
