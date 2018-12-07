package server.player;

import server.board.BoardSide;
import server.field.Pawn;

import java.util.List;

public class Player {
  private List<Pawn> pawns;
  private BoardSide startingSide;
  private Color color;

  public Player() {
  }
  public Player(BoardSide side, Color color) {
    this.startingSide = side;
    this.color = color;
  }
  public Player(List<Pawn> pawns, BoardSide side, Color color) {
    pawns.forEach(p -> p.setOwner(this));
    this.pawns = pawns;
    this.startingSide = side;
    this.color = color;
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
