package server.player;


import server.board.BoardSide;
import server.field.EmptyField;

import server.field.Field;
import server.field.Pawn;
import server.game.Game;
import server.player.botAlgorithm.BotAlgorithmTemplate;
import server.player.botAlgorithm.BotSender;


import java.util.ArrayList;
import java.util.List;

public class Bot extends Player {

  private List<EmptyField> targets;
  private Pawn chosenPawn;
  private boolean isMovable;
  private List<Field> lastPositions;
  private BotAlgorithmTemplate botTurn;
  private BotSender sender;
  private Game game;

  public Bot(BoardSide side, Color color, Game game, BotAlgorithmTemplate algorithm) {
    super(side, color);
    this.lastPositions = new ArrayList<>();
    this.targets = new ArrayList<>();
    this.isMovable = false;
    this.sender = new BotSender(this);
    this.botTurn = algorithm;
    this.game = game;
  }

  public void playTurn(){
    botTurn.play(this);
  }

  public BotSender getSender() {
    return sender;
  }

  public Game getGame() {
    return game;
  }

  public Pawn getChosenPawn() {
    return chosenPawn;
  }

  public void setChosenPawn(Pawn chosenPawn) {
    this.chosenPawn = chosenPawn;
  }

  public boolean isMovable() {
    return isMovable;
  }

  public void setMovable(boolean movable) {
    isMovable = movable;
  }

  public List<EmptyField> getTargets() {
    return targets;
  }

  public List<Field> getLastPositions() {
    return lastPositions;
  }
}
