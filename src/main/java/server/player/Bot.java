package server.player;


import server.board.BoardSide;
import server.field.Pawn;

import java.util.List;

public class Bot extends Player {
  public Bot(List<Pawn> pawns, BoardSide side, Color color) {
    super(pawns, side, color);
  }

  public Bot(){

  }

}