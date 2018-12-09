package server.player;

import server.board.BoardSide;
import server.field.Pawn;

import java.util.List;

public class Player {
  private int id;
  private List<Pawn> pawns;
  private BoardSide startingSide;
  private Color color;

  //0 - can't move, 1 - can move, 2 - can jump only, 3 - has won already
  private int moveToken;

  public Player() {
  }
  public Player(BoardSide side, Color color) {
    this.startingSide = side;
    this.color = color;
    this.moveToken = 0;
  }
  public Player(List<Pawn> pawns, BoardSide side, Color color) {
    pawns.forEach(p -> p.setOwner(this));
    this.pawns = pawns;
    this.startingSide = side;
    this.color = color;
    this.moveToken = 0;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getMoveToken() {
    return moveToken;
  }

  public void setMoveToken(int moveToken) {
    this.moveToken = moveToken;
  }

  public BoardSide getStartingSide() {
    return startingSide;
  }

  public List<Pawn> getPawns() {
    return pawns;
  }

  public Color getColor() {
    return color;
  }

  public void setPawns(List<Pawn> pawns) {
    pawns.forEach(p -> p.setOwner(this));
    this.pawns = pawns;
  }

  public void setStartingSide(BoardSide startingSide) {
    this.startingSide = startingSide;
  }

  public void setColor(Color color) {
    this.color = color;
  }
}
